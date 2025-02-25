import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Parser {
    private int currPos = 0;
    private HashMap<Integer, String> domainMap = new HashMap<>();
    public DnsMessage parse(DatagramPacket packet) {
        byte[] data = packet.getData();
        DnsHeader header = parseHeader(data);
        int qdcount = header.getQDCOUNT();
        currPos = 12;
        List<DnsQuestion> questions = new ArrayList<>();
        List<DnsAnswer> answers = new ArrayList<>();
        for (int i = 0; i < qdcount; i++)
            questions.add(parseQuestion(data));
        for (int i = 0; i < qdcount; i++)
            answers.add(parseAnswer(data));
        return new DnsMessage(header, questions, answers);
    }

    private DnsHeader parseHeader(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        short ID = buffer.getShort();
        short FLAGS = buffer.getShort();
        short QDCOUNT = buffer.getShort();
        short ANCOUNT = buffer.getShort();
        short NSCOUNT = buffer.getShort();
        short ARCOUNT = buffer.getShort();
        return new DnsHeader(ID, FLAGS, QDCOUNT, ANCOUNT, NSCOUNT, ARCOUNT);
    }

    private DnsQuestion parseQuestion(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.position(currPos);
        byte labelLength = buffer.get();
        StringBuilder labelBuilder = new StringBuilder();
        while (labelLength > 0) {
            if ((labelLength&192) == 192) {
                labelBuilder.append(".");
                int offset = ((labelLength&63) << 8) | (buffer.get() & 255);
                labelBuilder.append(domainMap.get(offset));
                break;
            }
            labelBuilder.append(new String(buffer.array(), buffer.position(), labelLength, StandardCharsets.UTF_8));
            buffer.position(buffer.position() + labelLength);
            labelLength = buffer.get();
            if (labelLength > 0) labelBuilder.append(".");
        }
        short QTYPE = buffer.getShort();
        short QCLASS = buffer.getShort();
        DnsQuestion question = new DnsQuestion(labelBuilder.toString(), QTYPE, QCLASS);
        domainMap.put(currPos, labelBuilder.toString());
        System.out.println(domainMap);
        currPos += question.getQuestionLength();
        return question;
    }

    private DnsAnswer parseAnswer(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.position(currPos);
        int labelLength = buffer.get();
        StringBuilder labelBuilder = new StringBuilder();
        while (labelLength > 0) {
            labelBuilder.append(new String(buffer.array(), buffer.position(), labelLength, StandardCharsets.UTF_8));
            buffer.position(buffer.position() + labelLength);
            labelLength = buffer.get();
            if (labelLength > 0) labelBuilder.append(".");
        }
        short QTYPE = buffer.getShort();
        short QCLASS = buffer.getShort();
        int TTL = buffer.getInt();
        short RDLENGTH = buffer.getShort();
        DnsAnswer answer = new DnsAnswer(labelBuilder.toString(), QTYPE, QCLASS, TTL, RDLENGTH);
        currPos += answer.getAnswerLength();
        return answer;
    }
}

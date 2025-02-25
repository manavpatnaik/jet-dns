import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public DnsMessage parse(DatagramPacket packet) {
        byte[] data = packet.getData();
        DnsHeader header = parseHeader(data);
        DnsQuestion question = parseQuestion(data);
        DnsAnswer answer = parseAnswer(data, 12+question.getQuestionLength());
        return new DnsMessage(header, question, answer);
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
        buffer.position(12);
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
        return new DnsQuestion(labelBuilder.toString(), QTYPE, QCLASS);
    }

    private DnsAnswer parseAnswer(byte[] data, int startPos) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.position(startPos);
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
        return new DnsAnswer(labelBuilder.toString(), QTYPE, QCLASS, TTL, RDLENGTH);
    }
}

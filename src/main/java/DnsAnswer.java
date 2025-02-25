import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class DnsAnswer {
    private short QTYPE;
    private short QCLASS;
    private int TTL;
    private short RDLENGTH;
    private String question;
    private String answer;

    public DnsAnswer(String question, short QTYPE, short QCLASS, int TTL, short RDLENGTH) {
        this.question = question;
        this.QTYPE = QTYPE;
        this.QCLASS = QCLASS;
        this.TTL = 60;
        this.RDLENGTH = 4;
        this.answer = "8.8.8.8";
    }

    public byte[] getAnswer() {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        buffer.put(encodeDomainName(question));
        buffer.putShort(QTYPE);
        buffer.putShort(QCLASS);
        buffer.putInt(TTL);
        buffer.putShort(RDLENGTH);
        buffer.put(encodeIpAddress(answer));
        return Arrays.copyOf(buffer.array(), buffer.position());
    }

    private byte[] encodeIpAddress(String s) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (String octet : s.split("\\.")) {
            out.writeBytes(octet.getBytes());
        }
        return out.toByteArray();
    }

    private byte[] encodeDomainName(String s) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (String label : s.split("\\.")) {
            int len = label.length();
            out.write(len);
            out.writeBytes(label.getBytes());
        }
        out.write(0);
        return out.toByteArray();
    }
}

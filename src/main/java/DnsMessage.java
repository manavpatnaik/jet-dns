import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class DnsMessage {
    private short id = 1234;
    private short flags = (short) 0b10000000_00000000;
    private short qdcount = 1;
    private short ancount = 1;
    private short nscount;
    private short arcount;
    private String domainName = "codecrafters.io";

    public DnsMessage() {}

    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(512);
        writeHeader(buffer);
        writeQuestion(buffer);
        writeAnswer(buffer);
        return buffer.array();
    }

    private void writeHeader(ByteBuffer buffer) {
        buffer.putShort(id)
                .putShort(flags)
                .putShort(qdcount)
                .putShort(ancount)
                .putShort(nscount)
                .putShort(arcount);
    }

    private void writeQuestion(ByteBuffer buffer) {
        short qtype = (short) 1;
        short qclass = (short) 1;
        buffer.put(encodeDomainName(domainName));
        buffer.putShort(qtype);
        buffer.putShort(qclass);
    }

    private void writeAnswer(ByteBuffer buffer) {
        short qtype = (short) 1;
        short qclass = (short) 1;
        int ttl = 60;
        short length = (short) 4;
        byte[] data = encodeIpAddress("8.8.8.8");
        buffer.put(encodeDomainName(domainName));
        buffer.putShort(qtype);
        buffer.putShort(qclass);
        buffer.putInt(ttl);
        buffer.putShort(length);
        buffer.put(data);
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

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class DnsMessage {
    private DnsHeader header;
    private DnsQuestion question;
    private DnsAnswer answer;

    public DnsMessage(DnsHeader header, DnsQuestion question, DnsAnswer answer) {
        this.header = header;
        this.question = question;
        this.answer = answer;
    }

    public byte[] getMessage() {
        return ByteBuffer.allocate(512)
                .put(header.getHeader())
                .put(question.getQuestion())
                .put(answer.getAnswer())
                .array();
    }

}

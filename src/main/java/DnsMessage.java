import java.io.ByteArrayOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;

public class DnsMessage {
    private DnsHeader header;
    private List<DnsQuestion> questions;
    private List<DnsAnswer> answers;

    public DnsMessage(DnsHeader header, List<DnsQuestion> questions, List<DnsAnswer> answers) {
        this.header = header;
        this.questions = questions;
        this.answers = answers;
    }

    public byte[] getMessage() {
        ByteBuffer buffer = ByteBuffer.allocate(512)
                .put(header.getHeader());
        for (DnsQuestion question : questions)
            buffer.put(question.getQuestion());
        for (DnsAnswer answer : answers)
            buffer.put(answer.getAnswer());
        return buffer.array();
    }

}

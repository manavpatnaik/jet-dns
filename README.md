# Jet-DNS

Jet-DNS is a lightweight and efficient DNS server implementation in Java, designed to handle DNS query parsing and forwarding with minimal overhead. It acts as an intermediary between clients and an upstream resolver, parsing incoming DNS requests, forwarding them to a resolver, and returning the appropriate responses.

## Features

### 🛠 Core Functionalities
- **UDP-Based DNS Server**: Listens for DNS queries over UDP on port `2053`.
- **Query Forwarding**: Forwards incoming DNS queries to an upstream resolver (e.g., Google's `8.8.8.8`).
- **Multiple Query Handling**: Supports handling multiple questions in a single DNS request.
- **Response Aggregation**: Receives responses from the resolver and constructs a combined response for the client.
- **Header Management**: Properly updates DNS headers for both requests and responses.
- **Error Handling**: Gracefully handles timeouts and missing responses.

### 📦 Protocol Support
- Supports standard DNS query types, including `A` (IPv4 address lookup).
- Implements a basic DNS message parser and encoder.

## Prerequisites
Ensure you have the following installed:
- Java 11 or later
- Maven (for dependency management and building the project)

## How to Run

### 🔹 1. Clone the Repository
```sh
 git clone https://github.com/manavpatnaik/jet-dns.git
 cd jet-dns
```

### 🔹 2. Build the Project
Use Maven to build the project:
```sh
mvn clean package
```

### 🔹 3. Run the DNS Server
Execute the server with:
```sh
java -jar target/jet-dns.jar 8.8.8.8:53
```
This starts the DNS server on `127.0.0.1:2053`, forwarding queries to the resolver at `8.8.8.8:53`.

Make sure that the resolver provides well formatted DNS answers.

### 🔹 4. Test the Server
You can use `dig` to send queries:
```sh
dig @127.0.0.1 -p 2053 example.com
```

## Project Structure
```
jet-dns/
│── src/main/java/
│   ├── Main.java               # Entry point for the DNS server
│   ├── DnsMessage.java         # Handles DNS message parsing and encoding
│   ├── DnsHeader.java          # Handles DNS header fields
│   ├── DnsQuestion.java        # Represents a DNS question
│   ├── DnsAnswer.java          # Represents a DNS answer
│   ├── Parser.java             # Parses incoming DNS messages
│── target/                     # Compiled output (after build)
│── pom.xml                     # Maven configuration file
│── README.md                   # Project documentation
```

## Troubleshooting
- **"DNS query failed: i/o timeout"**: Ensure the upstream resolver is reachable.
- **Port binding error**: Make sure port `2053` is not occupied.
- **"NoSuchElementException"**: Ensure the request is properly formed and contains valid data.

## License
This project is licensed under the MIT License. See `LICENSE` for details.

## Contact
For any queries or contributions, reach out via [LinkedIn](https://www.linkedin.com/in/manav-patnaik/).


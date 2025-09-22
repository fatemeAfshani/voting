#  Voting-Bot

## 🤝 How to Contribute and Commit

### Protobuf
make sure to have correct package name inside your .proto file.
#### ```option go_package = "proto/[YOUR_SERVICE]/golang";```
#### Example:
option go_package = "proto/user/golang";
### How to generate .go files from .proto file
```bash
protoc --go_out=. --go-grpc_out=. protos/[YOUR_SERVICE]/proto/[YOUR_PROTO_FILE.proto]
```
#### Example:
```bash
protoc --go_out=. --go-grpc_out=. protos/user/proto/user.proto
```

Happy coding! 🚀
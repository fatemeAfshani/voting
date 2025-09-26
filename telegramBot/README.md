#  Voting-Bot

## 🤝 How to Contribute and Commit

### Protobuf
make sure to have correct package name inside your .proto file.
#### ```option go_package = "proto/[YOUR_SERVICE]/golang";```
#### Example:
option go_package = "proto/user/golang";
### How to generate .go files from .proto file
```bash
protoc --experimental_allow_proto3_optional --go_out=. --go-grpc_out=. proto/[YOUR_SERVICE]/[YOUR_PROTO_FILE.proto]
```
#### Example:
```bash
protoc  --experimental_allow_proto3_optional --go_out=. --go-grpc_out=. proto/user/user.proto
```

Happy coding! 🚀
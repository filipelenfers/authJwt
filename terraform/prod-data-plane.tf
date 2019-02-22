
provider "aws" {
  region     = "us-east-1"
}

resource "aws_dynamodb_table" "users-dynamodb-table" {
  name           = "Users"
  read_capacity  = 2
  write_capacity = 1
  hash_key       = "email"

  attribute {
    name = "email"
    type = "S"
  }

  tags {
    Name        = "Users"
    Environment = "production"
  }
}

resource "aws_dynamodb_table_item" "dev-quickstart" {
  table_name = "${aws_dynamodb_table.users-dynamodb-table.name}"
  hash_key = "${aws_dynamodb_table.users-dynamodb-table.hash_key}"
  item = <<ITEM
{
  "email": {
    "S": "testUser@fakeEmail.com"
  },
  "password": {
    "S": "E400834ADA84140B779C587FBA306CF74CF50DCAE21967CB0C17134BD8A77D12"
  },
  "scopes": {
    "L": [
      {
        "S": "helloWorld:read"
      }
    ]
  }
}
ITEM
}
variable "environment" {
  type = "string"
  default = "dev"
}

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

resource "aws_ssm_parameter" "secret" {
  name        = "/${var.environment}/jwt/algorithm/secret"
  description = "Secret for hcma"
  type        = "SecureString"
  value       = "2BA7F56A04BA587C19F456D92AB04ADE5FBF5BA933F809B76B0C1346384B2602"
  overwrite   = true

  tags = {
    environment = "${var.environment}"
  }
}

resource "aws_ssm_parameter" "token_audience" {
  name        = "/${var.environment}/jwt/audience"
  description = "Audience to be set on jwt token"
  type        = "StringList"
  value       = "DevApis,BetaApis"
  overwrite   = true

  tags = {
    environment = "${var.environment}"
  }
}

resource "aws_ssm_parameter" "token_expiration" {
  name        = "/${var.environment}/jwt/expirationInMinutes"
  description = "Expiration in minutos for the jwt token"
  type        = "String"
  value       = "60"
  overwrite   = true

  tags = {
    environment = "${var.environment}"
  }
}

resource "aws_ssm_parameter" "token_issuer" {
  name        = "/${var.environment}/jwt/issuer"
  description = "Token issuer"
  type        = "SecureString"
  value       = "lenferslab"
  overwrite   = true

  tags = {
    environment = "${var.environment}"
  }
}

resource "aws_ssm_parameter" "token_nb" {
  name        = "/${var.environment}/jwt/nbSeconds"
  description = "Not before secons for the token"
  type        = "String"
  value       = "30"
  overwrite   = true

  tags = {
    environment = "${var.environment}"
  }
}
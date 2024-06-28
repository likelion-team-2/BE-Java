```markdown
# Order App

Order App is an application that allows users to place orders online.

## Quick Start

- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows)
- Install [Docker](https://docs.docker.com/get-docker/)

## Clone project

```bash
git clone https://github.com/MinhSang97/order_app.git
```

## Move to oneship folder

```bash
cd order_app
```
## Install container docker

```bash
docker compose up -d
```

## Install dependencies

```bash
go mod tidy
```

## DB Migration

```bash
make migrate-up
```

## Run project

```bash
go run ./cmd/main.go
```

## LICENSE

This project is distributed under the MIT License. See the `LICENSE` file for more information.
```

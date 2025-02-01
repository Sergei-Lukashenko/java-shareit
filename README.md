# java-shareit
Template repository for Shareit project.

## DataBase

```mermaid
erDiagram
users{
    bigint id PK
    varchar(255) name
    varchar(255) email UK
}

items{
    bigint id PK
    bigint owner_id FK
    varchar(255) name
    varchar(255) description
    boolean available
}

bookings{
    bigint id PK
    bigint booker_id FK
    bigint item_id FK
    timestamp start_date
    timestamp end_date
    varchar(10) status
}

comments{
    bigint id PK
    bigint item_id FK
    bigint author_id FK
    varchar(255) text
    timestamp created
}

requests{
    bigint id PK
    varchar(512) description
    timestamp created
    bigint requester_id FK
}

requested_items{
    bigint id PK
    bigint item_id FK
    bigint request_id FK
    timestamp created
}

users ||--o{ items: owner_id
users ||--o{ bookings: booker_id
items ||--o{ bookings: item_id
users ||--o{ comments: author_id
items ||--o{ comments: item_id
users ||--o{ requests: requester_id
items ||--o{ requested_items: item_id
requests ||--o{ requested_items: request_id

```
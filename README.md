# Automated Specification Attribute Extraction System

## 🔍 Overview

This project is an **Automated Specification Attribute Extraction system** built using modern backend, data pipeline, and AI technologies. It is designed to **streamline the extraction of product specification attributes** from unstructured inputs (text/images) and **improve product listing quality** in marketplaces and e-commerce platforms.

---

## 🧰 Tech Stack

- **Spring Boot** – Backend services and APIs
- **MongoDB** – NoSQL database for storing structured/unstructured product data
- **Apache Kafka** – Asynchronous communication & event-driven data flow
- **Apache Airflow** – Orchestration and scheduling of data pipelines
- **Generative AI & LLMs** – Extract specifications using NLP and prompt-based techniques
- **Prompt Engineering** – Controlled interactions with foundation models to extract accurate attributes

---

## 🚀 Key Features

- Automatic extraction of specification attributes from product descriptions or raw listings
- Integration with Large Language Models (LLMs) for intelligent parsing
- Modular data pipeline powered by **Kafka** and **Airflow**
- Easily extendable to support image-based attribute extraction
- Designed for scalability and real-time processing


## 📁 Project Structure

**autospec-data-processing/**
├── domain/                  ← Core business logic and model
│   ├── model/
│   │   └── ProductFeedItem.java
│   ├── port/
│   │   ├── in/              ← Use Cases (Input Port Interfaces)
│   │   │   └── ProductFeedUseCase.java
│   │   └── out/             ← External Interfaces (Output Ports)
│   │       └── ProductFeedRepositoryPort.java
│   └── service/
│       └── ProductFeedService.java  ← Implements Use Case
│
├── adapter/
│   ├── in/web/              ← Web Adapter (REST Controller)
│   │   └── ProductFeedController.java
│   └── out/persistence/     ← DB Adapter
│       ├── ProductFeedItemMongoAdapter.java
│       └── ProductFeedItemRepository.java
│
└── config/
└── BeanConfig.java



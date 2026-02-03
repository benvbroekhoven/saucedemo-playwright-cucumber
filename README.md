🌟 SauceDemo Playwright + Cucumber Framework

      

Een modern, schaalbaar en enterprise‑ready testautomatiseringsframework gebouwd met Playwright (Java), Cucumber BDD, JUnit, Allure Reporting en GitHub Actions. Dit project toont mijn expertise als Senior QA Automation Engineer / Test Architect in het ontwerpen van robuuste, onderhoudbare en CI‑gedreven testframeworks.

🚀 Highlights

✔ Moderne teststack

Playwright Java voor snelle, stabiele browserautomatisatie

Cucumber BDD voor leesbare scenario’s

JUnit als runner

Allure voor rijke rapportage

✔ Enterprise‑kwaliteit architectuur

Page Object Model

Custom BasePage met veilige acties & waits

Parallel execution

Schone projectstructuur

✔ Volledige CI/CD pipeline

GitHub Actions: build → test → report → deploy

Automatische Allure‑publicatie naar GitHub Pages

Trend reporting, categories, executors & environment info

🧱 Architectuurdiagram

                          ┌──────────────────────────┐
                          │      Test Runner         │
                          │   (JUnit + Cucumber)     │
                          └─────────────┬────────────┘
                                        │
                                        ▼
                         ┌──────────────────────────────┐
                         │        Feature Files          │
                         │        (Gherkin BDD)          │
                         └─────────────┬────────────────┘
                                       │
                                       ▼
                         ┌──────────────────────────────┐
                         │       Step Definitions        │
                         │  (Glue tussen BDD & Java)     │
                         └─────────────┬────────────────┘
                                       │
                                       ▼
                         ┌──────────────────────────────┐
                         │         Page Objects          │
                         │  (BasePage + Page Classes)    │
                         └─────────────┬────────────────┘
                                       │
                                       ▼
                         ┌──────────────────────────────┐
                         │      Playwright Factory       │
                         │  (Browser + Context + Page)   │
                         └─────────────┬────────────────┘
                                       │
                                       ▼
                         ┌──────────────────────────────┐
                         │       Playwright Engine       │
                         │ (Chromium / Firefox / WebKit) │
                         └──────────────────────────────┘

📂 Projectstructuur

saucedemo-playwright-cucumber/
│
├── pom.xml
├── README.md
├── index.html
│
├── src/
│   └── test/
│       ├── java/
│       │   ├── hooks/
│       │   ├── steps/
│       │   ├── pages/
│       │   └── runners/
│       │       └── TestRunner.java
│       │
│       └── resources/
│           ├── features/
│           ├── allure.properties
│           ├── environment.properties
│           ├── categories.json
│           └── executor.json
│
├── target/
│   └── allure-results/
│
└── .github/
    └── workflows/
        └── deploy.yml

🧪 Testen uitvoeren

▶ Lokaal testen

mvn clean test

▶ Lokaal Allure‑rapport openen

allure serve target/allure-results

🌐 Live Allure Report

https://benvbroekhoven.github.io/saucedemo-playwright-cucumber/allure-report/

Het rapport bevat:

Suites

Steps

Screenshots

Trend grafieken

Categories

Environment info

CI executor metadata

🛠️ Technologieën

Technologie

Rol

Java 21

Testautomatisatie

Playwright Java

Browserautomatisatie

Cucumber 7

BDD‑structuur

JUnit

Test runner

Maven

Build & dependency management

Allure

Rapportage

GitHub Actions

CI/CD

GitHub Pages

Hosting van rapporten

🧱 Architectuur

🔹 Page Object Model

Elke pagina heeft een eigen klasse

BasePage bevat alle waits, safe actions en navigatie

🔹 Cucumber BDD

Feature files in Gherkin

Steps in Java

Hooks voor setup/teardown

🔹 Playwright Factory

Eén centrale plek voor browser/page lifecycle

Ideaal voor parallel execution

🔹 Allure integratie

Cucumber plugin

Environment, categories, executors

Trend reporting via history‑preservation

🔄 CI/CD Pipeline

De pipeline:

Installeert Playwright dependencies

Draait alle tests

Downloadt vorige Allure history

Genereert een nieuw Allure‑rapport

Publiceert naar GitHub Pages

Update de README badge automatisch

Volledige workflow staat in:

.github/workflows/deploy.yml

🎯 Waarom dit project?

Dit framework demonstreert mijn vaardigheden in:

Testautomatiseringsarchitectuur

CI/CD integratie

Playwright expertise

BDD implementatie

Clean code & best practices

Enterprise‑ready workflows

Het is ontworpen als portfolio‑project om te laten zien hoe ik:

frameworks ontwerp

pipelines bouw

tooling integreer

kwaliteit borg

📬 Contact

Ben V. BroekhovenSenior QA Automation Engineer / Test ArchitectGitHub: https://github.com/benvbroekhoven
# OnePost Triage – PoC funkcionalita

Vytvořil jsem velmi zjednodušenou ukázkovou funkcionalitu pro aplikaci ONEPOST, která s využitím základní AI logiky dokáže automaticky vyhodnocovat příchozí zprávy. 
Funkcionalita třídí e-maily podle jejich důležitosti, rozpoznává lhůty uvedené v textu a zároveň poskytuje vysvětlení, proč konkrétní zprávě bylo přiřazeno dané hodnocení. 
Cílem je ukázat, jak by bylo možné v aplikaci ONEPOST přidat chytré seřazení a upozorňování na urgentní zprávy.

---

## Jak spustit

### Požadavky
- JDK 21 (nebo vyšší)
- Gradle (součástí projektu, není nutné mít globálně)

### Lokální spuštění

./gradlew bootRun


Po spuštění:
  Swagger UI (API dokumentace a testovací prostředí):
  http://localhost:8080/swagger-ui/index.html

  H2 konzole (náhled databáze):
  http://localhost:8080/h2-console
  JDBC URL: jdbc:h2:file:./data/triage-db
  User: sa
  Password: (prázdné)

.PHONY: run

run:
	@echo "Loading environment variables from .env..."
	@export $$(cat .env | xargs) && mvn spring-boot:run

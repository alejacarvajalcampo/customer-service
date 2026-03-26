DROP TABLE IF EXISTS outbox_event;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS personas;

CREATE TABLE personas (
    persona_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    edad INTEGER NOT NULL CHECK (edad >= 0),
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(150) NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

CREATE TABLE clientes (
    persona_id BIGINT PRIMARY KEY,
    cliente_id BIGINT NOT NULL UNIQUE,
    contrasena VARCHAR(120) NOT NULL,
    estado BOOLEAN NOT NULL,
    CONSTRAINT fk_clientes_personas
        FOREIGN KEY (persona_id)
        REFERENCES personas (persona_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_personas_identificacion ON personas (identificacion);
CREATE INDEX idx_clientes_cliente_id ON clientes (cliente_id);

CREATE TABLE outbox_event (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload LONGTEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    attempts INTEGER NOT NULL,
    last_error VARCHAR(500),
    created_at DATETIME NOT NULL,
    published_at DATETIME NULL
);

CREATE INDEX idx_outbox_status_id ON outbox_event (status, id);

INSERT INTO personas (
    persona_id,
    nombre,
    genero,
    edad,
    identificacion,
    direccion,
    telefono
) VALUES
    (1, 'Jose Lema', 'Masculino', 30, '1234567890', 'Otavalo sn y principal', '098254785'),
    (2, 'Marianela Montalvo', 'Femenino', 28, '9876543210', 'Amazonas y NNUU', '097548965'),
    (3, 'Juan Osorio', 'Masculino', 35, '5678901234', '13 junio y Equinoccial', '098874587');

INSERT INTO clientes (
    persona_id,
    cliente_id,
    contrasena,
    estado
) VALUES
    (1, 1, '1234', TRUE),
    (2, 2, '5678', TRUE),
    (3, 3, '1245', TRUE);
ALTER TABLE personas AUTO_INCREMENT = 4;

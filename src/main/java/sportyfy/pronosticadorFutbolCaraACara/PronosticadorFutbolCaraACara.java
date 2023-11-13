package sportyfy.pronosticadorFutbolCaraACara;

import lombok.Data;
import sportyfy.core.Pronosticador;
import sportyfy.core.entidades.equipo.Equipo;
import sportyfy.core.entidades.partido.Partido;
import sportyfy.core.entidades.resultado.Resultado;
import sportyfy.core.servicios.factorys.ResultadoPartidoFactory;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PronosticadorFutbolCaraACara implements Pronosticador {

    private Map<Partido, Resultado> partidosHistoricos;
    private final Logger logger = Logger.getLogger(PronosticadorFutbolCaraACara.class.getName());

    private CalculadorEnfrentamientosCaraACara calculador = new CalculadorEnfrentamientosCaraACara();

    @Override
    public void iniciar(String rutacCarpetaPartidos) {
        partidosHistoricos = ResultadoPartidoFactory.crearPartidosResultado(rutacCarpetaPartidos,
                new ObjectMapper());
    }

    @Override
    public Resultado pronosticar(Partido partido) {
        System.out.println("Pronosticando partido: " + partido);
        return calculador.ganadorEnfrentamientosCaraACara(partido, partidosHistoricos);
    }

    @Override
    public String getDeporte() {
        return "Fútbol";
    }

    @Override
    public Set<Equipo> getEquipos() {
        return partidosHistoricos.keySet().stream()
                .flatMap(partido -> Stream.of(partido.getLocal(), partido.getVisitante()))
                .collect(Collectors.toSet());
    }

}
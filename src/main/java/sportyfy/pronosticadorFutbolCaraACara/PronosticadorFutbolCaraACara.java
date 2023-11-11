package sportyfy.pronosticadorFutbolCaraACara;



import lombok.Data;
import sportyfy.core.Pronosticador;
import sportyfy.core.entidades.equipo.Equipo;
import sportyfy.core.entidades.partido.Partido;
import sportyfy.core.entidades.resultado.Resultado;
import sportyfy.core.servicios.factorys.ResultadoPartidoFactory;
import sportyfy.core.servicios.parser.EquiposParser;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PronosticadorFutbolCaraACara implements Pronosticador {

    private Map<Partido,Resultado> partidosHistoricos;
    private final Logger logger = Logger.getLogger(PronosticadorFutbolCaraACara.class.getName());

    private CalculadorEnfrentamientosCaraACara calculador;

    public PronosticadorFutbolCaraACara () {
        iniciar();
    }
    @Override
    public void iniciar() {
        try {
            Set<Equipo> equipos = new EquiposParser().crearEquiposDesdeArchivos("src/main/resources/datos/partidos");
            partidosHistoricos = ResultadoPartidoFactory.crearPartidosResultado("src/main/resources/datos/partidos",
                    new ObjectMapper(), equipos);
            Set<Partido> partidos = partidosHistoricos.keySet();
            for(Partido partido : partidos){
                System.out.println(partido.getLocal().getNombre() + "," + partido.getVisitante().getNombre());
            }

            calculador = new CalculadorEnfrentamientosCaraACara();
        } catch (IOException e) {
            logger.severe("Error al leer los archivos de partidos");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resultado pronosticar(Partido partido) {
        return calculador.ganadorEnfrentamientosCaraACara(partido,partidosHistoricos);
    }

    @Override
    public String getDeporte() {
        return "Fútbol";
    }

    @Override
    public Set<Equipo> getEquipos() {
        Set<Equipo> equipos = new HashSet<>();
        for (Partido partido : partidosHistoricos.keySet()) {
            equipos.add(partido.getLocal());
            equipos.add(partido.getVisitante());
        }
        return equipos;
    }

}
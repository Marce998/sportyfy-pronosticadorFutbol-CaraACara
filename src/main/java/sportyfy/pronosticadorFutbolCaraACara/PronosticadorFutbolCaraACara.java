package sportyfy.pronosticadorFutbolCaraACara;

import sportyfy.core.Pronosticador;
import sportyfy.core.Pronostico;
import sportyfy.core.PronosticoNull;
import sportyfy.core.entidades.equipo.Equipo;
import sportyfy.core.entidades.partido.PartidoFuturo;
import sportyfy.core.entidades.partido.PartidoJugado;

import java.util.List;
import java.util.stream.Collectors;

public class PronosticadorFutbolCaraACara implements Pronosticador {

    @Override
    public Pronostico pronosticar(PartidoFuturo partidoFuturo, List<PartidoJugado> partidosJugados) {
        Equipo local = partidoFuturo.getEquipoLocal();
        Equipo visitante = partidoFuturo.getEquipoVisitante();

        validarDatos(local,visitante,partidosJugados);

        List<PartidoJugado> enfrentamientosEntreSi = ultimosEnfrentamientosEntreAmbosEquipos(local,visitante,partidosJugados);
        if(enfrentamientosEntreSi.isEmpty()){
            throw new IllegalArgumentException("No se encontraron partidos entre ambos equipos para realizar el pronóstico");
        }

        Equipo equipoGanador = pronosticarCaraACara(local,visitante,enfrentamientosEntreSi);

        return equipoGanador!=null ? new Pronostico(equipoGanador, partidoFuturo) : new PronosticoNull(partidoFuturo);
    }

    private void validarDatos(Equipo local, Equipo visitante, List<PartidoJugado> partidosJugados) {
        if(local==null || visitante==null){
            throw new IllegalArgumentException("No se puede realizar el pronóstico con equipos nulos");
        }

        if(partidosJugados.isEmpty()){
            throw new IllegalArgumentException("No hay información de partidos para realizar el pronóstico");
        }
    }

    private List<PartidoJugado> ultimosEnfrentamientosEntreAmbosEquipos(Equipo local, Equipo visitante, List<PartidoJugado> partidosJugados) {
        return  partidosJugados.stream()
                .filter(partido -> partido.participa(local) && partido.participa(visitante))
                .collect(Collectors.toList());
    }

    private Equipo pronosticarCaraACara(Equipo local, Equipo visitante, List<PartidoJugado> enfrentamientosEntreSi) {
        int ganadosLocal = partidosGanadosEquipo(local,enfrentamientosEntreSi);
        int ganadosVisitante = partidosGanadosEquipo(visitante,enfrentamientosEntreSi);

        if(ganadosLocal > ganadosVisitante) return local;
        if(ganadosVisitante > ganadosLocal) return visitante;
        return null;

    }

    private int partidosGanadosEquipo(Equipo equipo, List<PartidoJugado> enfrentamientosEntreSi) {
        return (int) enfrentamientosEntreSi.stream().filter(partido -> partido.obtenerGanador() != null
                && partido.obtenerGanador().equals(equipo)).count();
    }

    @Override
    public String obtenerDeporte() {
        return "Fútbol";
    }

}
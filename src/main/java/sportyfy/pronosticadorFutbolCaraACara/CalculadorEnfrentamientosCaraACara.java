package sportyfy.pronosticadorFutbolCaraACara;

import sportyfy.core.entidades.equipo.Equipo;
import sportyfy.core.entidades.partido.Partido;
import sportyfy.core.entidades.resultado.Resultado;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CalculadorEnfrentamientosCaraACara {

    int golesLocal = 0;
    int golesVisitante = 0;
    Map <Partido,Resultado> partidosFiltrados = new HashMap<Partido,Resultado>();

    public Resultado ganadorEnfrentamientosCaraACara(Partido partido, Map<Partido, Resultado> partidos){
        Equipo local = partido.getLocal();
        Equipo visitante = partido.getVisitante();

        Map<Partido,Resultado> enfrentamientosEntreSi = enfrentamientosEntreSi(local,visitante,partidos);
        if(enfrentamientosEntreSi.isEmpty()){
            throw new IllegalArgumentException("No hay información de partidos para realizar el pronóstico");
        }

        return calcularGanador(local,visitante,enfrentamientosEntreSi);

    }

    public Map<Partido,Resultado> enfrentamientosEntreSi(Equipo local,Equipo visitante,Map<Partido,Resultado> partidos){
       return partidos.entrySet().stream()
                .filter(entrada -> (entrada.getKey().getLocal().getNombre().equals(local.getNombre()) && entrada.getKey().getVisitante().getNombre().equals(visitante.getNombre()))
                        || entrada.getKey().getLocal().getNombre().equals(visitante.getNombre()) && entrada.getKey().getVisitante().getNombre().equals(local.getNombre()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Resultado calcularGanador(Equipo local, Equipo visitante,Map<Partido,Resultado> partidosEntreSi){

        int ganadosLocal = cantidadPartidosGanadosEquipo(local,partidosEntreSi);
        int ganadosVisitante = cantidadPartidosGanadosEquipo(visitante,partidosEntreSi);

        if(ganadosLocal > ganadosVisitante){
            partidosFiltrados = partidosGanadosEquipo(local,partidosEntreSi);
            golesLocal = promedioGolesEquipo(local,partidosFiltrados);
            golesVisitante = promedioGolesEquipo(visitante,partidosFiltrados);
        }
        else if (ganadosVisitante > ganadosLocal){
            partidosFiltrados = partidosGanadosEquipo(visitante,partidosEntreSi);
            golesLocal = promedioGolesEquipo(local,partidosFiltrados);
            golesVisitante = promedioGolesEquipo(visitante,partidosFiltrados);
        }

        return new Resultado(new LinkedHashMap<>(Map.of(local,golesLocal,visitante,golesVisitante)));

    }

    public int cantidadPartidosGanadosEquipo(Equipo equipo,Map<Partido,Resultado> partidos){
        return (int) partidos.entrySet().stream()
                .filter(entrada -> entrada.getKey().getGanador(entrada.getValue()).map(ganador -> ganador.getNombre().equals(equipo.getNombre())).orElse(false))
                .count();
    }

    public Map<Partido,Resultado> partidosGanadosEquipo(Equipo equipo,Map<Partido,Resultado> partidos){
        return partidos.entrySet().stream()
                .filter(entrada -> entrada.getKey().getGanador(entrada.getValue()).map(ganador -> ganador.getNombre().equals(equipo.getNombre())).orElse(false))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public int promedioGolesEquipo(Equipo equipo, Map<Partido,Resultado> partidos){
        return (int) Math.round(partidos.values().stream()
                .mapToInt(resultado -> resultado.getMarcador(equipo).orElse(0))
                .average()
                .orElse(0));
    }

}

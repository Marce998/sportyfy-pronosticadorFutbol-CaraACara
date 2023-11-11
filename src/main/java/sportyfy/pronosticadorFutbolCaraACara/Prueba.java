package sportyfy.pronosticadorFutbolCaraACara;

import sportyfy.core.entidades.equipo.Equipo;
import sportyfy.core.entidades.partido.Partido;
import sportyfy.core.entidades.resultado.Resultado;

public class Prueba {


    public static void main (String[] args){
        Equipo river = new Equipo("River Plate");
        Equipo ccse = new Equipo("Central Cordoba de Santiago");
        Partido partido = new Partido(river,ccse);
        PronosticadorFutbolCaraACara pronosticador = new PronosticadorFutbolCaraACara();

        Resultado resultado = pronosticador.pronosticar(partido);
        pronosticador.iniciar();
        System.out.println(pronosticador.getEquipos().contains(river));
        System.out.println(pronosticador.getEquipos().toString());
        System.out.println(partido.getLocal().getNombre() + " ," + partido.getVisitante().getNombre());
        System.out.println(resultado.toString());
    }
}

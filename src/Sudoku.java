import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * <h2>Sudoku Completed Generator</h2>
 * @author Criis<br>
 * Github -> CriisTeck
 * @version 1.0
 */
public class Sudoku {
    /**
     * Arreglo con los numeros posibles en una casilla de sudoku.
     */
    private final Integer[] possibleNumbers = {5, 7, 6, 1, 4, 2, 3, 9, 8};

    public static void main(String[] args) {
        new Sudoku().doSudok(0);
    }


    /**
     * Inicializa la matriz principal del sudoku que se compone de la siguiente manera:<br><br>
     * <pre>
     *   | ------ | ------ | ------ |
     *   | [][][] | [][][] | [][][] |
     *   | [][][] | [][][] | [][][] |
     *   | [][][] | [][][] | [][][] |
     *   | ------ | ------ | ------ |
     *   | [][][] | [][][] | [][][] |
     *   | [][][] | [][][] | [][][] |
     *   | [][][] | [][][] | [][][] |
     *   | ------ | ------ | ------ |
     *   | [][][] | [][][] | [][][] |
     *   | [][][] | [][][] | [][][] |
     *   | [][][] | [][][] | [][][] |
     *   | ------ | ------ | ------ |
     *   </pre>
     *
     *   La cual es una matriz principal 3x3 y en cada espacio de esta matriz se almacena una nueva matriz 3x3
     *
     * @param counter Contador encargado de tener la cantidad de veces que se genero un sudoku sin una solución.
     */
    public void doSudok(int counter) {
        Object[][] principal = new Object[3][3];
        for (int i = 0; i < principal.length; i++)
            for (int j = 0; j < principal.length; j++) principal[i][j] = new Integer[3][3];
        try {
            solve(principal);
            showSudoku(principal);
            System.out.println("\n\n Sudokus fallidos : " + counter);
        } catch (Exception e) {

            doSudok(++counter);
        }
    }

    /**
     * Asigna el numero que cumple las reglas del sudoku en la casilla correspondiente.
     * @param principal Matriz principal del sudoku.
     */
    private void solve(Object[][] principal) {
        for (int i = 0; i < principal.length; i++) {
            for (int j = 0; j < principal[i].length; j++) {
                Integer[][] section = (Integer[][]) principal[i][j];
                for (int k = 0; k < section.length; k++) {
                    for (int l = 0; l < section[k].length; l++) {
                        section[k][l] = localizateNumber(section, principal, i, j, k, l);
                    }
                }
                principal[i][j] = section;
            }

        }
    }

    /**
     * Metodo encargado de evaluar y aplicar las reglas basicas del sudoku y en base a estas encontrar un numero que
     * cumpla las condiciones en todo el tablero.
     * @param section Matriz 3x3 individual, en donde ningun numero debe repetirse en los 9 espacios.
     * @param principal Matriz principal que contiene el tablero en su totalidad.
     * @param filaExtern Fila en la cual esta la seccion a evaluar y que se usara para evaluar una regla.
     * @param columnExtern Columna en la cual esta la seccion a evaluar y que se usará para evaluar una regla.
     * @param filaIntern Fila en la cual esta el numero a evaluar y que se usara para evaluar una regla.
     * @param columnIntern Columna en la cual esta el numero a evaluar y que se usará para evaluar una regla.
     * @return Retorna el numero que cumpla las reglas tanto horizontal, vertical como dentro de la matriz interna.
     */
    private Integer localizateNumber(Integer[][] section, Object[][] principal, int filaExtern, int columnExtern, int filaIntern, int columnIntern) {
        List<Integer> list = new ArrayList<>();
        List<Integer> listV = new ArrayList<>();
        List<Integer> listH = new ArrayList<>();
        Collections.shuffle(Arrays.asList(possibleNumbers));
        for (int k = 0; k < principal.length; k++) {
            Object[][] vertical = (Object[][]) principal[k][columnExtern];
            Object[][] horizontal = (Object[][]) principal[filaExtern][k];
            Arrays.stream(vertical).map(objects -> (Integer) objects[columnIntern]).forEach(listV::add);
            IntStream.range(0, horizontal.length).mapToObj(i -> (Integer) horizontal[filaIntern][i]).forEach(listH::add);
        }

        Arrays.stream(section).map(Arrays::asList).forEach(list::addAll);

        /*
        -------Solo con propositos de Debug------

        List<Integer> listOne = new ArrayList<>();
        List<Integer> listTwo = new ArrayList<>();
        List<Integer> listThree = new ArrayList<>();

        listOne = Arrays.stream(possibleNumbers).filter(o -> list.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).collect(Collectors.toList());
        listTwo = Arrays.stream(possibleNumbers).filter(o -> list.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).filter(o -> listV.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).collect(Collectors.toList());
        listThree = Arrays.stream(possibleNumbers).filter(o -> list.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).filter(o -> listV.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).filter(o -> listH.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).collect(Collectors.toList());
         */
        return Arrays.stream(possibleNumbers).filter(o -> list.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).filter(o -> listV.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).filter(o -> listH.stream().noneMatch(h -> h != null && h.compareTo(o) == 0)).findAny().get();
    }

    /**
     * Metodo encargado de mostrar el sudoku.
     * @param principal Matriz principal donde esta almacenado el tablero en cuestion.
     */
    private void showSudoku(Object[][] principal) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            list.add(new ArrayList<>());
        }
        int i = 0;
        int k = 0;
        int counterList = 0;
        int counterAux = 0;
        for (; i < principal.length; i++) {
            Integer[][] aux = (Integer[][]) principal[i][counterAux];
            for (int j = 0; j < aux.length; j++) {
                list.get(counterList).add(aux[k][j]);
            }
            counterAux++;
            if (counterAux == 3) {
                counterList++;
                k++;
                counterAux = 0;
            }
            if (k == 3) {
                k = 0;
                continue;
            }
            i--;
        }
        list.forEach(System.out::println);
    }
}
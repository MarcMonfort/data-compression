/**
 * @file AlgoritmoLZ78.java
 * @Author Marc Monfort
 * @brief Implementación del algoritmo de compresión/descompresión LZ78
 */
package compresor.dominio.algoritmo;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/** @class AlgoritmoLZ78
 @brief Compresión/descompresión mediante LZ78
 */
public class AlgoritmoLZ78 implements Algoritmo {
    private static final int MAX = 65535;

    /**
     * @brief Compresión de un archivo aplicando el algoritmo LZ78
     * @param entrada
     * @return Archivo comprimido
     */
    @Override
    public byte[] comprimir(byte[] entrada) {

        List<Byte> salida = new ArrayList<Byte>();

        TernarySearchTree tst = new TernarySearchTree();

        byte character;
        List<Byte> prefix = new ArrayList<Byte>();
        int id = 1;

        for(int i = 0; i < entrada.length; ++i)
        {
            character = entrada[i];

            List<Byte> aux = new ArrayList<Byte>(prefix);   //puede que este por referencia...
            aux.add(character);
            if (tst.search(aux)) {

                prefix.add(character);
            }

            else
            {

                if (prefix.isEmpty()) {
                    int nada = 0;
                    salida.add((byte)(nada >>> 8));
                    salida.add((byte) nada);

                    //salida.add((byte) 0,(byte) 0); //deberia ser < 256
                    //salida.add((byte) 0); funciona lo de arriba?????

                }

                else {
                    //salida.add((byte)tst.getId(prefix));
                    int idp = tst.getId(prefix);                //error puede que se lea al revés
                    salida.add((byte)((idp >>> 8) & 0xFF));
                    salida.add((byte) (idp & 0xFF));




                }

                salida.add(character);

                if (id <= MAX) {//no supera el indice maximo 256?? mejor aumentar este numero....
                    prefix.add(character);
                    tst.insert(prefix, id);
                    ++id;
                }
                //System.out.println(id);
                prefix = new ArrayList<Byte>();


            }
        }

        if (prefix.isEmpty()) {
            salida.add((byte) 0);
            salida.add((byte) 0);
        }
        else {
            //salida.add((byte) tst.getId(prefix));
            int idp = tst.getId(prefix);                //error puede que se lea al revés
            salida.add((byte) (idp >>> 8));
            salida.add((byte) idp);
        }

        byte[] elarray = new byte[salida.size()];
        for(int i = 0; i < salida.size(); ++i) {
            //System.out.println(salida.get(i));
            elarray[i] = salida.get(i);
        }



        return elarray;
    }

    /**
     * @brief Descompresión de un archivo comprimido mediante LZ78
     * @param entrada
     * @return Archivo descomprimido
     */
    @Override
    public byte[] descomprimir(byte[] entrada) {

        List<Byte> salida = new ArrayList<Byte>();

        HashMap<Integer, List<Byte>> dictionary = new HashMap<Integer, List<Byte>>();

        byte character;
        int prefix;


        for(int i = 0; i < entrada.length -2; i += 3)
        {

            //prefix = (entrada[i] & 0xFF);
            prefix = ((entrada[i] & 0xFF) << 8) | (entrada[i+1] & 0xFF);    //dos bytes por num
            //System.out.println("adeu  " + prefix);
            character = entrada[i+2];

            if (prefix != 0) {
                List<Byte> pp = dictionary.get(prefix); //error de referencia con slida???
                List<Byte> haha = new ArrayList<Byte>(pp);
                salida.addAll(haha);    //se puede poner pp

                List<Byte> ppc = new ArrayList<Byte>(pp);
                ppc.add(character); //no se puede poner pp
                dictionary.put(dictionary.size() + 1, ppc);
            }
            else {
                List<Byte> aux = new ArrayList<Byte>();
                aux.add(character);
                dictionary.put(dictionary.size() + 1, aux);
            }

            salida.add(character);

        }

        //prefix = (entrada[entrada.length -1] & 0xFF);
        prefix = ((entrada[entrada.length -2] & 0xFF) << 8) | (entrada[entrada.length -1] & 0xFF);
        if (prefix != 0) salida.addAll(dictionary.get(prefix));


        byte[] elarray = new byte[salida.size()];
        for(int i = 0; i < salida.size(); i++) elarray[i] = salida.get(i);

        return elarray;

    }
}

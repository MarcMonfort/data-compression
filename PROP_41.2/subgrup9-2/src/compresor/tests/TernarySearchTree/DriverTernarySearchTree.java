package compresor.tests.TernarySearchTree;

import compresor.dominio.algoritmo.TernarySearchTree;

import java.util.*;

public class DriverTernarySearchTree {

    public static List<Byte> toListByte(String name){

        List<Byte> word = new ArrayList<Byte>();
        byte[] b = name.getBytes();
        for(byte text:b) {
            word.add(text);
        }
        return word;
    }

    public static void main (String [] args){

        TernarySearchTree tst = new TernarySearchTree();
        System.out.println("Driver Ternary Search Tree\n");

        Scanner scan = new Scanner(System.in);

        List<String> insert = new ArrayList<>();
        List<Integer> insertID = new ArrayList<>();

        String word = scan.next();

        while(!word.equals("TEST")) {
            insert.add(word);
            int id = scan.nextInt();
            insertID.add(id);
            word = scan.next();
        }

        List<String> test = new ArrayList<>();
        while(scan.hasNext()) {
            word = scan.next();
            test.add(word);
        }



        int numErrores = 0;

        for(int i = 0; i < insert.size(); ++i) {
            try {
                String palabra = insert.get(i);
                int id = insertID.get(i);
                tst.insert(toListByte(palabra), id);

            }catch (Exception e) {
                System.out.println("ERROR: " + e + "\n");
            }

        }


        for(String palabra:test) {
            try {
                    int id;
                    System.out.println("palabra: " + palabra);

                    System.out.println("search: " + tst.search(toListByte(palabra)));
                    try {
                        System.out.println("getId: " + tst.getId(toListByte(palabra)) + "\n");
                    } catch (Exception e) {
                        System.out.println("getID: ERROR\n");
                    }

            }catch (Exception e) {
                System.out.println("ERROR: " + e + "\n");
            }

        }

    }
}

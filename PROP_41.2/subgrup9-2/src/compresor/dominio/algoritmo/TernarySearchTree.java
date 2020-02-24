/**
 * @file TernarySearchTree.java
 * @Author Marc Monfort
 * @brief Implementación de un árbol de búsqueda ternario
 */

package compresor.dominio.algoritmo;


import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;


/**
 * @class Node
 * @brief Estructura y constructor de un nodo del árbol
 */
class Node
{
    byte data;
    boolean isEnd;
    int id;
    Node left, middle, right;

    /**
     * @brief Constructora de un nodo
     * @param data
     * @param id
     * @return Crea un nodo con el contenido y el identificador especificados por parámetro
     */
    public Node(byte data, int id)
    {
        this.data = data;
        this.isEnd = false;
        this.id = id;
        this.left = null;
        this.middle = null;
        this.right = null;
    }
}

/**
 * @class TernarySearchTree
 * @brief Implementación del árbol de búsqueda ternario
 */
public class TernarySearchTree
{
    private Node root;

    /**
     * @brief Constructora por defecto
     */
    public TernarySearchTree()
    {
        root = null;
    }

    /**
     * @brief Método público para almacenar una nueva cadena en el árbol
     * @param word
     * @param id
     */
    public void insert(List<Byte> word, int id)
    {
        root = insert(root, word, 0, id);
    }

    /**
     * @brief Inserta, para cada letra de la palabra, un nuevo nodo en el árbol, en la posición que le corresponda,
     * con el identificador y por contenido una letra de la palabra especificados por parámetro
     * @param r
     * @param word
     * @param i
     * @param id
     * @return
     */
    public Node insert(Node r, List<Byte> word, int i, int id)
    {
        if (r == null){
            r = new Node(word.get(i), id);
        }

        if (word.get(i) < r.data){
            r.left = insert(r.left, word, i, id);
        }
        else if (word.get(i) > r.data){
            r.right = insert(r.right, word, i, id);
        }
        else
        {
            if (i + 1 < word.size()){
                r.middle = insert(r.middle, word, i + 1, id);
            }
            else{
                r.isEnd = true;
                r.id = id;
            }
        }
        return r;
    }

    /**
     * @brief Método público de búsqueda en el árbol
     * @param word
     * @return true si la palabra buscada existe en el árbol, false si no
     */
    public boolean search(List<Byte> word)
    {
        return search(root, word, 0);
    }

    /**
     * @brief Búsqueda de una palabra en el árbol
     * @param r
     * @param word
     * @param i
     * @return true si la palabra existe en el árbol, false si no
     */
    private boolean search(Node r, List<Byte> word, int i)
    {
        if (r == null)
            return false;

        if (word.get(i) < r.data)
            return search(r.left, word, i);
        else if (word.get(i) > r.data)
            return search(r.right, word, i);
        else
        {
            if (r.isEnd && i == word.size() - 1)
                return true;
            else if (i == word.size() - 1)
                return false;
            else
                return search(r.middle, word, i + 1);
        }
    }

    /**
     * @brief Método público para obtener el identificador de la palabra especificada
     * @param word
     * @return Devuelve el identificador asociado a la palabra especificada
     */
    public int getId(List<Byte> word)
    {
        return getId(root, word, 0);
    }

    /**
     * @brief Recupera el identificador asociado a una palabra del árbol
     * @param r
     * @param word
     * @param i
     * @return Devuelve el identificador asociado a la palabra especificada
     */
    private int getId(Node r, List<Byte> word, int i)
    {
        if (word.get(i) < r.data){
            return getId(r.left, word, i);
        }
        else if (word.get(i) > r.data)
            return getId(r.right, word, i);
        else
        {
            if (r.isEnd && i == word.size() - 1){
                return r.id;

            }
            else
                return getId(r.middle, word, i + 1);
        }
    }
}

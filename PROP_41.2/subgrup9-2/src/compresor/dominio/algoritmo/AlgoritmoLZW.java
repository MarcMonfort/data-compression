/**
 * @file AlgoritmoLZW.java
 * @Author Daniel Donate
 * @brief Implementación del algoritmo de compresión/descompresión LZW
 */

package compresor.dominio.algoritmo;

import java.util.*;

/** @class AlgoritmoLZW
 @brief Compresión/descompresión mediante LZW
 */
public class AlgoritmoLZW implements Algoritmo {
	/**
	 * @brief Compresión de un archivo aplicando el algoritmo LZW
	 * @param datosAComprimir
	 * @return Archivo comprimido
	 */
	@Override
	public byte[] comprimir(byte[] datosAComprimir) {
		HashMap<String, Integer> dictionary = new HashMap<>();
		byte input_byte; int byte_value;
		String W = "", WS="";
		char C;
		int Size = 256;

		byte[] buffer = new byte[3];
		boolean onLeft = true;

		// Precarga del diccionario
		for(int i = 0; i < 256; i++) dictionary.put(Character.toString((char)i),i);

		List<Byte> output = new ArrayList<Byte>();

		if (datosAComprimir.length == 0) {byte[] r = new byte[0]; return r;}
		input_byte = datosAComprimir[0];  // Leemos primer byte para inicializar W
		byte_value = new Byte(input_byte).intValue();

		if(byte_value < 0) byte_value += 256;
		C = (char) byte_value; W = ""+C;

		int k = 1;

		while (k < datosAComprimir.length) {
			input_byte = datosAComprimir[k];
			byte_value = new Byte(input_byte).intValue();

			if (byte_value < 0) byte_value += 256;
			C = (char) byte_value;

			/* si W+C ya está en el diccionario */
			if (dictionary.containsKey(W+C)) W = W+C;

			else {
				WS = this.convertir12Bits(dictionary.get(W));
				if(onLeft) {
					buffer[0] = (byte) Integer.parseInt(WS.substring(0,8),2);
					buffer[1] = (byte) Integer.parseInt(WS.substring(8,12)+"0000",2);
				}

				/* Tomamos el valor de W y lo convertimos a 12 bits para evitar problemas
				   con los caracteres especiales codificados con 8 bits	*/

				else {
					buffer[1] += (byte) Integer.parseInt(WS.substring(0,4),2);
					buffer[2] = (byte) Integer.parseInt(WS.substring(4,12),2);
					for (int i = 0; i < buffer.length; i++) {
						output.add(buffer[i]);
						buffer[i] = 0;
					}
				}
				onLeft = !onLeft;
				if (Size < 4096) dictionary.put(W+C, Size++);

				W =""+C; //Reseteamos W y empezamos con 'C'
			}

			k++;
		}

        /*
            Si onLeft => current data en WS va a buffer[0] y buffer[1], y eso es lo que escribimos.
			Si no, guardamos el current WS y escribimos los 3 bytes.
        */
		WS = this.convertir12Bits(dictionary.get(W));
		if(onLeft) {
			buffer[0] = (byte) Integer.parseInt(WS.substring(0,8),2);
			buffer[1] = (byte) Integer.parseInt(WS.substring(8,12)+"0000",2);
			output.add(buffer[0]);
			output.add(buffer[1]);
		}
		else {
			buffer[1] += (byte) Integer.parseInt(WS.substring(0, 4), 2);
			buffer[2] = (byte) Integer.parseInt(WS.substring(4, 12), 2);
			for (int i = 0; i < buffer.length; i++) {
				output.add(buffer[i]);
				buffer[i] = 0;
			}
		}

		byte[] compressed_datosAComprimir = new byte[output.size()];
		for(int i = 0; i < output.size(); ++i) {
			compressed_datosAComprimir[i] = output.get(i);
		}
		return compressed_datosAComprimir;
	}

	/**
	 * @brief Convierte un entero a una cadena binaria de 12 bits
	 * @param i
	 * @return Cadena con la representación binaria en 12 bits de un entero
	 */
	private String convertir12Bits(int i) {
		String to12Bit = Integer.toBinaryString(i);
		while (to12Bit.length() < 12) to12Bit = "0" + to12Bit;
		return to12Bit;
	}

	/**
	 * @brief Descompresión de un archivo comprimido aplicando el algoritmo LZW
	 * @param entrada
	 * @return Archivo descomprimido
	 */
	@Override
	public byte[] descomprimir(byte[] entrada) {
		String[] arrayOfChar = new String[4096];
		int Size = 256;
		int currentword, previousword;

		byte[] buffer = new byte[3];
		boolean onLeft = true;

		for (int i = 0; i < 256; i++) arrayOfChar[i] = Character.toString((char)i);

		List<Byte> output = new ArrayList<Byte>();

		buffer[0] = entrada[0];
		buffer[1] = entrada[1];
		previousword = this.getIntValue(buffer[0], buffer[1], onLeft);
		onLeft = !onLeft;

		byte[] aux1 = (arrayOfChar[previousword]).getBytes();
		for (int i = 0; i < aux1.length; i++) output.add(aux1[i]);

		int k = 2;

		while (k < entrada.length) {

			if (onLeft) {
				buffer[0] = entrada[k++];
				buffer[1] = entrada[k++];
				currentword = this.getIntValue(buffer[0], buffer[1], onLeft);
			}
			else {
				buffer[2] = entrada[k++];
				currentword = this.getIntValue(buffer[1], buffer[2], onLeft);
			}
			onLeft = !onLeft;

			/*
			 currentword no está en dictionary => añadimos previousword in la entrada.
			*/

			if (currentword >= Size) {
				if (Size < 4096) {
					arrayOfChar[Size] = arrayOfChar[previousword] + arrayOfChar[previousword].charAt(0);
				}
				Size++;
				byte[] aux2 = (arrayOfChar[previousword] + arrayOfChar[previousword].charAt(0)).getBytes();
				for (int i = 0; i < aux2.length; i++) output.add(aux2[i]);
			}

			else {
				if (Size < 4096) {
					arrayOfChar[Size] = arrayOfChar[previousword] + arrayOfChar[currentword].charAt(0);
				}
				Size++;

				byte[] aux3 = (arrayOfChar[currentword]).getBytes();
				for (int i = 0; i < aux3.length; i++) output.add(aux3[i]);
			}
			previousword = currentword;
		}

		byte[] decompressed_entrada = new byte[output.size()];
		for(int i = 0; i < output.size(); ++i) {
			decompressed_entrada[i] = output.get(i);
		}
		return decompressed_entrada;
	}

	/**
	 * @brief Calcula el valor decimal de una cadena de 12 bits
	 * @param b1
	 * @param b2
	 * @param onLeft
	 * @return Valor decimal codificado en los 12 bits de la cadena de entrada
	 */
	private int getIntValue(byte b1, byte b2, boolean onLeft) {
		String t1 = Integer.toBinaryString(b1);
		String t2 = Integer.toBinaryString(b2);

		while (t1.length() < 8) t1 = "0" + t1;
		if (t1.length() == 32) t1 = t1.substring(24, 32);

		while (t2.length() < 8) t2 = "0" + t2;
		if (t2.length() == 32) t2 = t2.substring(24, 32);

		if (onLeft) return Integer.parseInt(t1 + t2.substring(0, 4), 2);
		else return Integer.parseInt(t1.substring(4, 8) + t2, 2);

	}
}

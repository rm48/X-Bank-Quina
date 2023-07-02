package quina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cadastro {

	public static final String ARQUIVO = "cadastro.txt";
	
	/**
     * Adiciona o Cliente, passado via parametro
     * @param clientToTxt
     * @throws IOException
     */
    public static void gravaCliente(ContaTxt clienteToTxt) throws IOException{
    	FileWriter arquivo = new FileWriter(ARQUIVO,true);
    	PrintWriter gravarArquivo = new PrintWriter(arquivo);
    	gravarArquivo.println(clienteToTxt.gerarString());
    	arquivo.flush();	//libera a gravaçao
    	arquivo.close();	//fecha o arquivo
    }
 // FIM de gravaCliente
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //
     /*
     * Retorna as linhas de cadastro.txt no formato List
     */
    public static List<ContaTxt> leCntas() throws IOException{
    	 //Lista que vamos retornar
        List<ContaTxt> listOnTxt = new ArrayList<ContaTxt>(0);
        FileReader arq = new FileReader(ARQUIVO);
        //armazenando conteudo no arquivo no buffer
        BufferedReader lerArq = new BufferedReader(arq);
        String linha = lerArq.readLine();//primeiralinha
        //criando objeto clientela para popular a listOnTxt
        ContaTxt clientela = new ContaTxt(linha);//ja pega a linha 0
        listOnTxt.add(clientela);        
        //a variavel linha recebe o valor 'null' quando chegar no final do arquivo
        while (linha != null){
            //System.out.printf("%s\n",linha);
            //lendo a segundo até a última
            linha = lerArq.readLine();
            // Passamos a linha para popular o objeto, 
            // se não for vazia
            if(null != linha && !"".equals(linha) ){
                clientela = new ContaTxt(linha);
                listOnTxt.add(clientela);
            }
        }
        arq.close();
        return listOnTxt;
    }
    // FIM de leCntas
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //
    /*
     * Atualiza uma linha no arquivo cadastro.txt
     */
    public static void replaceLinha( String linhaAlterar, String linhaAlterada) {
    File f = new File(ARQUIVO);    
    File nf = new File("temporario.tmp");
        FileWriter fw = null;
        Scanner s = null;
        try {
            fw = new FileWriter(nf);
            s = new Scanner(f);

            while (s.hasNextLine()) {
                String linha = s.nextLine();

                linha = linha.replace(linhaAlterar, linhaAlterada);

                try {
                    fw.write(linha + System.getProperty("line.separator"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        f.delete();
        nf.renameTo(f);
    }
    // FIM de replaceLinha
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
     //
     /*
     * Exclui uma linha no arquivo cadastro.txt
     */
    public static void excluirLinha(String contaAExcluir) {                                                  
        FileReader fileReader = null;  
        FileWriter fileWriter = null;   
        BufferedReader leitor = null;  
        String nomeDoArquivo = ARQUIVO;
        File orig = new File(nomeDoArquivo);
        String arquivoConferir = "conferir2.txt";
        File novo = new File(arquivoConferir);
        String line = "";  
            try {  
                fileReader = new FileReader(new File(nomeDoArquivo));  
                fileWriter = new FileWriter(new File(arquivoConferir)); 
                leitor = new BufferedReader(fileReader);
                line = "";  
                while ((line = leitor.readLine()) != null) {  
                    if(!line.trim().equals(contaAExcluir.trim())) { 
                            fileWriter.write(line + "\r\n");      
                    }  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally   {  
                try {  
                    fileWriter.close();
                    fileReader.close();
                    orig.delete();
                    novo.renameTo(orig);
                } catch (IOException e) {  
                    e.printStackTrace();  
                } 
            } 
    }
    // FIM de excluirLinha
}

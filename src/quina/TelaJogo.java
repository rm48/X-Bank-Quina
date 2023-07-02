package quina;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;

public class TelaJogo extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel painelNumeros, painelBotoes, painelInfo;
	private JLabel labelDica;
	private JButton botaoApostar,  botaoBank, botaoSaldo,
			botaoValores, botaoChances, botaoResultados, botaoAcertos;
	private JButton posicoesFiguras[];
	private Icon imagemApostar,  imagemBank, imagemSaldo,
			imagemValores, imagemChances, imagemResultados, imagemAcertos;
	private Icon[] indigos, azuis, rubros, dourados;
	private GridLayout gridNumeros, gridBotoes, gridInfo;
	private Container container;
	
	int numerosTotal = 80, apostaMinima = 5, apostaMaxima = 15, concursoAtual=0;
	int[] numerosCartela, acertos, sorteados;
	int palpite = 0, bola = 0;
	int premioMinimo=2;
	float valorAposta;
	private boolean proxConc = false, temAcerto = false, contaDispnvl = false;
	boolean saldoBoleano,
			objResCriado=false, objAceCriado=false;;
	static boolean transferiu = false;
	
	Date data;
    SimpleDateFormat formatada;
	DecimalFormat d = new DecimalFormat("00");
	DecimalFormat df = new DecimalFormat("##,#00.00");
	Random numeroAleatorio = new Random();
	ArrayList listaRsltds = new ArrayList();
    ArrayList listaAcrts = new ArrayList();
    Popup objResultados, objAcertos;
    String dadosContaString;//"nome,senha,conta,saldo,credito"
    
    // - - - - - - - - -// - - - - - - - - -// - - - - - - - -
    // - - - - - - - - C O N S T R U C T O R  - - - - - - - - 
	public TelaJogo() {
		super("Quina 0.2");
		abrirArquivo();
		acertos = new int[apostaMinima];
		sorteados = new int[apostaMinima];
		numerosCartela = new int[numerosTotal];
		container = getContentPane();

		imagemApostar = new ImageIcon(getClass().getResource("/img/apostar.gif"));
		botaoApostar = new JButton("Apostar", imagemApostar);
		botaoApostar.addActionListener(this);
		
		
		imagemBank = new ImageIcon(getClass().getResource("/img/bank.png"));
		botaoBank = new JButton("Banco", imagemBank);
		botaoBank.addActionListener(this);
		
		imagemSaldo = new ImageIcon(getClass().getResource("/img/saldo.png"));
		botaoSaldo = new JButton("Saldo", imagemSaldo);
		botaoSaldo.addActionListener(this);
		

		imagemValores = new ImageIcon(getClass().getResource("/img/valores.png"));
		botaoValores = new JButton("Valores", imagemValores);
		botaoValores.addActionListener(this);

		imagemChances = new ImageIcon(getClass().getResource("/img/chances.png"));
		botaoChances = new JButton("Chances", imagemChances);
		botaoChances.addActionListener(this);

		imagemResultados = new ImageIcon(getClass().getResource("/img/resultados.png"));
		botaoResultados = new JButton("Resultados", imagemResultados);
		botaoResultados.addActionListener(this);
		
		imagemAcertos = new ImageIcon(getClass().getResource("/img/acertos.png"));
		botaoAcertos = new JButton("Acertos", imagemAcertos);
		botaoAcertos.addActionListener(this);

		painelNumeros = new JPanel();
		painelBotoes = new JPanel();
		painelInfo = new JPanel();
		
		labelDica = new JLabel("Quina 0.2");
		labelDica.setText("Proximo concurso: "+Integer.toString(concursoAtual+1));

		gridNumeros = new GridLayout(8, 10, 1, 1);
		gridBotoes = new GridLayout(7, 1, 1, 1);
		gridInfo = new GridLayout(1,1,10,10);

		painelBotoes.setLayout(gridBotoes);
		painelNumeros.setLayout(gridNumeros);
		painelNumeros.setBackground(Color.darkGray);
		indigos = new Icon[numerosTotal];
		azuis = new Icon[numerosTotal];
		rubros = new Icon[numerosTotal];
		dourados = new Icon[numerosTotal];

		posicoesFiguras = new JButton[numerosTotal];

		for (int i = 0; i < numerosTotal; i++) {
			indigos[i] = new ImageIcon(getClass().getResource("/img/indigo/" + (i + 1) + ".gif"));
			azuis[i] = new ImageIcon(getClass().getResource("/img/azul/" + (i + 1) + ".gif"));
			rubros[i] = new ImageIcon(getClass().getResource("/img/rubro/" + (i + 1) + ".gif"));
			dourados[i] = new ImageIcon(getClass().getResource("/img/ouro/" + (i + 1) + ".gif"));
			this.posicoesFiguras[i] = new JButton();
			this.posicoesFiguras[i].setName(Integer.toString(i));
			posicoesFiguras[i].setIcon(indigos[i]);
			posicoesFiguras[i].setActionCommand(posicoesFiguras[i].getName());
			posicoesFiguras[i].addActionListener(this);
			painelNumeros.add(posicoesFiguras[i]);
		}
		container.add(painelBotoes, BorderLayout.WEST);
		painelBotoes.add(botaoApostar);
		painelBotoes.add(botaoSaldo);
		painelBotoes.add(botaoBank);
		painelBotoes.add(botaoValores);
		painelBotoes.add(botaoChances);
		painelBotoes.add(botaoResultados);
		painelBotoes.add(botaoAcertos);
		painelInfo.add(labelDica);
		container.add(painelNumeros);
		container.add(painelInfo,BorderLayout.SOUTH);
		setSize(900, 650);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	} // fim do construtor TelaJogo
	
	//------------------------------------------------------------------------------
	// EVENTOS DE CLIQUE (MENU / BOTÃO)
	//------------------------------------------------------------------------------
	public void actionPerformed(ActionEvent evento) {
		
// APOSTAR 		
		if (evento.getSource() == botaoApostar) {
			if (!proxConc){
				valorAposta = tabelaValor(palpite);
				if (!(contaDispnvl)){ 
	                JOptionPane.showMessageDialog(painelNumeros,"Voce precisa acessar uma conta primeiro.");
	            }
				else if (palpite >= apostaMinima) {
					
					if (saldoBoleano = Atm.contatxtLidaAtual.sacar(valorAposta)) {
						int contaAcertos = 0;
						concursoAtual++;
						labelDica.setText("Resultado do concurso: "+Integer.toString(concursoAtual));
						for (int i = 0; i < apostaMinima; i++) {
							do {
								bola = numeroAleatorio.nextInt(numerosTotal);
							} while (numerosCartela[bola] % 2 == 1);
							if (numerosCartela[bola] == 2) {
								numerosCartela[bola] = 3;
								acertos[contaAcertos] = bola + 1; // adiciona ao array acertos
								contaAcertos++;
							} else {
								numerosCartela[bola] = 1; // marca a posição com 1 (já sorteado)
							}
							sorteados[i] = bola + 1; // adiciona ao array sorteados
						}
						ordenar();
						String res = "";
						res+=concursoAtual+": ";
						for (int num : sorteados) {
							if (num<10)
								res +="0"+num + " ";
							else
								res += num + " ";
						}
						listaRsltds.add(res);
//	salva resultados (conc)
                        try {
                            OutputStream os = new FileOutputStream("conc", true);
                            OutputStreamWriter osw = new OutputStreamWriter(os);
                            BufferedWriter bw = new BufferedWriter(osw);
                            bw.write(res + "\r\n");
                            bw.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
//  pinta os icones  
						pintar();
//	 verifica acertos						
						if (contaAcertos >= premioMinimo){
							double premio = tabelaPremios(contaAcertos);
//E deposita na conta se for maior ou igual ao premio minimo  
							Atm.contatxtLidaAtual.depositar(premio);
	                           JOptionPane.showMessageDialog(painelNumeros, "Foi depositado em sua conta:\n  " + df.format(premio) + "\nParabéns!");
	                            String ace = "";
	                            ace+=concursoAtual+": ";
	                            for (int num=0;num < contaAcertos;num++) {
	                                if (acertos[num]<10)
	                                    ace += "0"+acertos[num] + " ";
	                                else
	                                ace += acertos[num] + " ";
	                            }
	                            listaAcrts.add(ace);
//	salva acertos   (acer)
	                            try {
	                                OutputStream os = new FileOutputStream("acer", true);
	                                OutputStreamWriter osw = new OutputStreamWriter(os);
	                                BufferedWriter bw = new BufferedWriter(osw);
	                                bw.write(ace + "\r\n");
	                                bw.close();
	                            } catch (Exception ex) {
	                                ex.printStackTrace();
	                            }
							}
	                        for (int i = 0; i < acertos.length; i++) {
	                        	acertos[i] = 0;
	                        }
	                        palpite = 0;
//	   GRAVAR NA CONTA (cadastro.txt)
	                        String linhaAlterada=Atm.contatxtLidaAtual.getNome()+","+Atm.contatxtLidaAtual.getSenha()
	                                    +","+Atm.contatxtLidaAtual.getConta()+","+Atm.contatxtLidaAtual.getSaldo()
	                                    +","+Atm.contatxtLidaAtual.getCredito();
	                        Cadastro.replaceLinha(dadosContaString, linhaAlterada) ;
	                        dadosContaString=Atm.contatxtLidaAtual.getNome()+","+Atm.contatxtLidaAtual.getSenha()
	                                    +","+Atm.contatxtLidaAtual.getConta()+","+Atm.contatxtLidaAtual.getSaldo()
	                                    +","+Atm.contatxtLidaAtual.getCredito();                                 
	                        proxConc=true;	                                                   
						} else {
                        	JOptionPane.showMessageDialog(painelNumeros, "Seu saldo é insuficiente.");
 						}// fim de:if saldo na conta, sacar(valorAposta) e apostar
					} else{
	                JOptionPane.showMessageDialog(painelNumeros, "A aposta mínima é de "+apostaMinima+" números");
					}// fim de: if palpite >= apostaMinima
				} else {
					limpar();
					proxConc = false;
				}// fim de if (!proxConc)
			}// fim if botaoApostar

// ACESSAR CONTATXT
        else if ( evento.getSource() == botaoBank) {
            Frame acesso = null;
            new Atm(acesso);
            if (!"".equals(Atm.nomeLog)) {
                dadosContaString=Atm.contatxtLidaAtual.getNome()+","+Atm.contatxtLidaAtual.getSenha()
                                    +","+Atm.contatxtLidaAtual.getConta()+","+Atm.saldoLog
                                    +","+Atm.contatxtLidaAtual.getCredito();         
                botaoSaldo.setEnabled(true);   
                contaDispnvl = true;
            }
            else{
                dadosContaString="";   
                botaoSaldo.setEnabled(false);   
                contaDispnvl = false;
            }
            
            //      GRAVAR NA CONTA (cadastro.txt)
            if (transferiu){
                            String linhaAlterada=Atm.contatxtLidaAtual.getNome()+","+Atm.contatxtLidaAtual.getSenha()
                                    +","+Atm.contatxtLidaAtual.getConta()+","+Atm.contatxtLidaAtual.getSaldo()
                                    +","+Atm.contatxtLidaAtual.getCredito();
                            Cadastro.replaceLinha(dadosContaString, linhaAlterada) ;
                            dadosContaString=Atm.contatxtLidaAtual.getNome()+","+Atm.contatxtLidaAtual.getSenha()
                                    +","+Atm.contatxtLidaAtual.getConta()+","+Atm.contatxtLidaAtual.getSaldo()
                                    +","+Atm.contatxtLidaAtual.getCredito();  
                            transferiu = false;
            }
        }
		
// SALDO       
        else if (evento.getSource() == botaoSaldo) {    
            JOptionPane.showMessageDialog(painelNumeros, "Saldo: " + df.format(Double.parseDouble(Atm.contatxtLidaAtual.getSaldo()))
                    + "\nLimite: " + df.format(Double.parseDouble(Atm.contatxtLidaAtual.getCredito())));
      
        } 
// VALORES
        else if (evento.getSource() == botaoValores) {
        	String tabela = "Valor da aposta:\n";
            for (int i = apostaMinima; i <= apostaMaxima; i++) {
                tabela += d.format(i) + " números:  \t" + df.format(tabelaValor(i)) + "\n";
            }
            JOptionPane.showMessageDialog(painelNumeros, tabela,"Valores",1);
		} 
// PRPBABILIDADES        
        else if (evento.getSource() == botaoChances) {
        	JOptionPane.showMessageDialog(painelNumeros,
    				"Conforme a aposta, uma chance em:\n" + "\n" + "Núm.----Quina------Quadra-----Terno-----Duque\n"
    						+ " 5:  24.040.016      64.106        866        36\n"
    						+ " 6:    4.006.669      21.658        445        25\n"
    						+ " 7:    1.144.763        9.409        261       18\n"
    						+ " 8:       429.286        4.770        168        14\n"
    						+ " 9:       190.794        2.687        115        12\n"
    						+ "10:        95.396        1.635         82          9\n"
    						+ "11:        52.035        1.056         62          8\n"
    						+ "12:        30.354           714         48          7\n"
    						+ "13:        18.679           502         38          6\n"
    						+ "14:        12.008           364         31         5,8\n"
    						+ "15:          8.005           271         25         5,2\n\n"
    						+ "\tQuina 0.1 - 07/2023 \n\treinaldo589@hotmail.com",
    				"Probabilidades", 1);
		}
 // RESULTADO       
        else if (evento.getSource() == botaoResultados) {
        	data = new Date();
            formatada = new SimpleDateFormat("dd/MM/yy - HH:mm:ss");
            String concs = "";
            for (int i = 0; i < listaRsltds.size(); i++) {
                concs += (String) listaRsltds.get(i) + "\n";
            }
            concs +="    ******************\n " +
            formatada.format(data) + "\n";
            if (objResCriado == true)
                objResultados.dispose();
            objResultados = new Popup(concs,"Resultados",75,175,200,600);
            objResCriado = true;   
		} 
// ACERTOS      
        else if (evento.getSource() == botaoAcertos) {
        	String acerts = "";
            for (int i = 0; i < listaAcrts.size(); i++) {
                acerts += (String) listaAcrts.get(i) + "\n";
            }
            if (objAceCriado == true)
                objAcertos.dispose();
            objAcertos = new Popup(acerts,"Acertos",275,475,200,300);
            objAceCriado = true;
		} 

		// -----------------------------------------------
		// Caso contrário é evento de botões de imagem
		// BOTOES 1 a numerosTotal
		else {
			int i = Integer.parseInt(evento.getActionCommand());
			marcar(i);
		}
	} // fim do método actionPerformed (Eventos de clique no menu e botões)

	
	
	//------------------------------------------------------------------------------
    public  void ordenar() {
		int i, j,aux;  
		for(i = 0; i < apostaMinima; i++){  
			for(j =0; j < apostaMinima-1; j++){  
				if(sorteados[j] > sorteados[j+1]){  
					aux = sorteados[j];  
					sorteados[j] = sorteados[j+1];  
					sorteados[j+1] = aux;  
				}  
			}  
		}              
	}// end ordenar 
    
	//------------------------------------------------------------------------------    
    public float tabelaValor(int bet) {
        switch(bet){
            case 5: return 2.5f;
            case 6: return 15.0f;
            case 7: return 52.5f;
            case 8: return 140f;
            case 9: return 315f;         
            case 10: return 630f;
            case 11: return 1155f;
            case 12: return 1980f;
            case 13: return 3217.5f;
            case 14: return 5005f;
            case 15: return 7507.5f;
            default:  return 0;
       }     
   }// end tabelaValor 

    //------------------------------------------------------------------------------    
    public double tabelaPremios(int num) {
        switch(num){
            case 2: return numeroAleatorio.nextInt(10)*50+50;//a500
            case 3: return numeroAleatorio.nextInt(10)*250+250;//a2500
            case 4: return numeroAleatorio.nextInt(10)*5000+5000;//a50.000
            case 5: return numeroAleatorio.nextInt(10)*1000000+1000000;// a 10.000.000
            default:  return 0;
       }     
   }// end tabelaPremios   

    // ===================================
	void marcar(int i) {
		if (!proxConc) {
			// desmarca
			if (numerosCartela[i] == 2) {
				posicoesFiguras[i].setIcon(indigos[i]);
				numerosCartela[i] = 0;
				palpite--;
			} else if (palpite >= apostaMaxima)
				JOptionPane.showMessageDialog(painelNumeros, "Atingiu a aposta máxima.");
			// marca
			else {
				posicoesFiguras[i].setIcon(azuis[i]);
				numerosCartela[i] = 2;
				palpite++;
			}
			if (palpite>=apostaMinima && palpite<=apostaMaxima){
                labelDica.setText("Valor da aposta: "+df.format(tabelaValor(palpite))+ " irreais");
            }
			else {
				labelDica.setText("Proximo concurso: "+ (concursoAtual+1));
			}
		} else {
			limpar();
		}
	} // fim do método marcar

	// ===================================
	//
	void pintar() {
		if (!proxConc) {
			painelNumeros.setBackground(Color.lightGray);
			for (int i = 0; i < numerosCartela.length; i++) {
				if (numerosCartela[i] == 2) {// apostou
					numerosCartela[i] = 0;// desmarca a posicao
				} else if (numerosCartela[i] == 3) {// acertou
					posicoesFiguras[i].setIcon(dourados[i]);
					numerosCartela[i] = 0;
				} else if (numerosCartela[i] == 1) {// errou
					posicoesFiguras[i].setIcon(rubros[i]);
					numerosCartela[i] = 0;
				} else {// desativa demais icones
					posicoesFiguras[i].setEnabled(false);
				}
			}
			proxConc = true;
		} else {
			limpar();
		}
	} // fim do método pintar

	// ===================================
	//

	void limpar() {
		painelNumeros.setBackground(Color.darkGray);
		for (int i = 0; i < numerosCartela.length; i++) {
			numerosCartela[i] = 0;
			posicoesFiguras[i].setIcon(indigos[i]);
			posicoesFiguras[i].setEnabled(true);
		}
		palpite = 0;
		proxConc = false;
	} // fim do método limpar

	
	//------------------------------------------------------------------------------    
    public void abrirArquivo() {
        //RESULTADOS
        InputStream isConc;
        try {
            isConc = new FileInputStream("conc");        
        InputStreamReader isr = new InputStreamReader(isConc);
        //armazenando conteudo no arquivo no buffer
        BufferedReader br = new BufferedReader(isr);
         String linha = br.readLine(); //primeiralinha
         //a variavel linha recebe o valor 'null' quando chegar no final do arq
        while (linha != null) {
            listaRsltds.add(linha);//System.out.println(s);
            linha = br.readLine();
        }
        concursoAtual=listaRsltds.size();
        br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //DOIS OU + ACERTOS
        InputStream isAcer;
        try {
            isAcer = new FileInputStream("acer");        
        InputStreamReader isr = new InputStreamReader(isAcer);
        BufferedReader br = new BufferedReader(isr);
         String s = br.readLine(); //primeiralinha
        while (s != null) {
            listaAcrts.add(s);//System.out.println(s);
            s = br.readLine();
        }
        if (listaAcrts.size()>0)
            temAcerto=true;
        br.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } 
    }
//-----------------------------------------------------------------------   

	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(TelaJogo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new TelaJogo().setVisible(true);
			}
		});

	}// fim da classe main

} // fim da classe TelaJogo

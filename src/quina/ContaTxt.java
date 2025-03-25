package quina;

class ContaTxt {
	public static final String SEPARADOR = ",";
    private String nome; 
    private String senha;
    private String conta;
    private String saldo;
    private String credito;

    public ContaTxt() {      }

    public ContaTxt(String line) { 
    	if(null != line ){
            String[] dados = line.split(SEPARADOR);
            if(null != dados){
                setNome(dados[0]);
                setSenha(dados[1]);
                setConta(dados[2]);
                setSaldo(dados[3]);
                setCredito(dados[4]);          
            }
        }   
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(null != nome){
            if(nome.contains(SEPARADOR)){
                nome = nome.replaceAll(SEPARADOR, " ");
            }
        }
        this.nome = nome;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
      
    public String getConta(){
        return conta;
    }
    private void setConta(String conta) {
        this.conta = conta;
    }
    public String getSaldo(){
        return saldo;
    }
    private void setSaldo(String saldo) {
        this.saldo = saldo;
    }
     public String getCredito(){
        return credito;
    }
    private void setCredito(String credito) {
        this.credito = credito;
    }
    
    public boolean sacar(double quantia){
    	double saldo = Double.parseDouble(this.saldo);
    	double credito = Double.parseDouble(this.credito);
    	if ((saldo + credito) >= quantia) {
    		saldo -= quantia;
    		this.saldo = Double.toString(saldo);
    		return true;
    	} else
    	return false;
    }
    
    public void depositar(double quantia){
        double saldo = Double.parseDouble(this.saldo);
        saldo += quantia;
        this.saldo = Double.toString(saldo);
    }
    
    public String gerarString(){
        final StringBuffer buffer = new StringBuffer();
        if(null != getNome()){
            buffer.append(getNome());
        }

        buffer.append(SEPARADOR);
        if(null != getSenha()){
            buffer.append(getSenha());
        }
        
        buffer.append(SEPARADOR);
        if(null != getConta()){
            buffer.append(getConta());
        }
        
        buffer.append(SEPARADOR);  
        buffer.append(getSaldo());
        
        buffer.append(SEPARADOR);
        buffer.append(getCredito());
        
        return buffer.toString();
    }
}

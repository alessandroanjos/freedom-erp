/**
 * @version 11/2007 <BR>
 * @author Setpoint Inform�tica Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.rep <BR>
 * Classe:
 * @(#)RelResumoDiario.java <BR>
 * 
 * Este programa � licenciado de acordo com a LPG-PC (Licen�a P�blica Geral para Programas de Computador), <BR>
 * vers�o 2.1.0 ou qualquer vers�o posterior. <BR>
 * A LPG-PC deve acompanhar todas PUBLICA��ES, DISTRIBUI��ES e REPRODU��ES deste Programa. <BR>
 * Caso uma c�pia da LPG-PC n�o esteja dispon�vel junto com este Programa, voc� pode contatar <BR>
 * o LICENCIADOR ou ent�o pegar uma c�pia em: <BR>
 * Licen�a: http://www.lpg.adv.br/licencas/lpgpc.rtf <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa � preciso estar <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Relatorio produtos por clientes.
 * 
 */

package org.freedom.modulos.rep;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.componentes.GuardaCampo;
import org.freedom.componentes.JRadioGroup;
import org.freedom.componentes.JTextFieldFK;
import org.freedom.componentes.JTextFieldPad;
import org.freedom.componentes.ListaCampos;
import org.freedom.funcoes.Funcoes;
import org.freedom.telas.Aplicativo;
import org.freedom.telas.FPrinterJob;
import org.freedom.telas.FRelatorio;

public class RelProdutoCliente extends FRelatorio {

	private static final long serialVersionUID = 1;

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private final JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private final JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JRadioGroup<String, String> rgOrdem;
	
	private final ListaCampos lcCliente = new ListaCampos( this );
	
	private final ListaCampos lcFornecedor = new ListaCampos( this );
	
	private final ListaCampos lcVendedor = new ListaCampos( this );
	
	private List<Object> prefere = new ArrayList<Object>();

	public RelProdutoCliente() {

		super( false );
		setTitulo( "Relatorio de Produto por Cliente" );		
		setAtribos( 100, 50, 325, 330 );
		
		montaRadioGrupos();
		montaListaCampos();
		montaTela();
		
		Calendar cal = Calendar.getInstance();			
		txtDtFim.setVlrDate( cal.getTime() );		
		cal.set( cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ) - 1, cal.get( Calendar.DATE ) );
		txtDtIni.setVlrDate( cal.getTime() );	
	}
	
	private void montaRadioGrupos() {
		
		Vector<String> labs2 = new Vector<String>();
		labs2.add( "C�digo" );
		labs2.add( "Raz�o social" );
		Vector<String> vals2 = new Vector<String>();
		vals2.add( "C" );
		vals2.add( "R" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, labs2, vals2 );
	}
	
	private void montaListaCampos() {
		
		/***********
		 * CLIENTE *
		 ***********/
		
		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "C�d.cli.", ListaCampos.DB_PK, false ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Raz�o social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.montaSql( false, "CLIENTE", "RP" );
		lcCliente.setQueryCommit( false );
		lcCliente.setReadOnly( true );
		txtCodCli.setListaCampos( lcCliente );
		txtCodCli.setTabelaExterna( lcCliente );
		txtCodCli.setPK( true );
		txtCodCli.setNomeCampo( "CodCli" );
		
		/**************
		 * FORNECEDOR *
		 **************/
		
		lcFornecedor.add( new GuardaCampo( txtCodFor, "CodFor", "C�d.for.", ListaCampos.DB_PK, false ) );
		lcFornecedor.add( new GuardaCampo( txtRazFor, "RazFor", "Raz�o social do fornecedor", ListaCampos.DB_SI, false ) );
		lcFornecedor.montaSql( false, "FORNECEDOR", "RP" );
		lcFornecedor.setQueryCommit( false );
		lcFornecedor.setReadOnly( true );
		txtCodFor.setListaCampos( lcFornecedor );
		txtCodFor.setTabelaExterna( lcFornecedor );
		txtCodFor.setPK( true );
		txtCodFor.setNomeCampo( "CodFor" );
		
		/************
		 * VENDEDOR *
		 ************/
		
		lcVendedor.add( new GuardaCampo( txtCodVend, "CodVend", "C�d.vend.", ListaCampos.DB_PK, false ) );
		lcVendedor.add( new GuardaCampo( txtNomeVend, "NomeVend", "Nome do vendedor", ListaCampos.DB_SI, false ) );
		lcVendedor.montaSql( false, "VENDEDOR", "RP" );
		lcVendedor.setQueryCommit( false );
		lcVendedor.setReadOnly( true );
		txtCodVend.setListaCampos( lcVendedor );
		txtCodVend.setTabelaExterna( lcVendedor );
		txtCodVend.setPK( true );
		txtCodVend.setNomeCampo( "CodVend" );
	}
	
	private void montaTela() {
		
		adic( new JLabel( "Ordem do relatorio :" ), 10, 10, 200, 20 );
		adic( rgOrdem, 10, 35, 290, 30 );
		
		JLabel periodo = new JLabel( "Periodo", SwingConstants.CENTER );
		periodo.setOpaque( true );
		adic( periodo, 25, 70, 60, 20 );
		
		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		adic( borda, 10, 80, 290, 45 );
		
		adic( txtDtIni, 25, 95, 110, 20 );
		adic( new JLabel( "at�", SwingConstants.CENTER ), 135, 95, 40, 20 );
		adic( txtDtFim, 175, 95, 110, 20 );
		
		adic( new JLabel( "C�d.for." ), 10, 130, 77, 20 );
		adic( txtCodFor, 10, 150, 77, 20 );
		adic( new JLabel( "Raz�o social do fornecedor" ), 90, 130, 210, 20 );
		adic( txtRazFor, 90, 150, 210, 20 );
		
		adic( new JLabel( "C�d.vend." ), 10, 170, 77, 20 );
		adic( txtCodVend, 10, 190, 77, 20 );
		adic( new JLabel( "Nome do vendedor" ), 90, 170, 210, 20 );
		adic( txtNomeVend, 90, 190, 210, 20 );
		
		adic( new JLabel( "C�d.cli." ), 10, 210, 77, 20 );
		adic( txtCodCli, 10, 230, 77, 20 );
		adic( new JLabel( "Raz�o social do cliente" ), 90, 210, 210, 20 );
		adic( txtRazCli, 90, 230, 210, 20 );
	}

	@ Override
	public void imprimir( boolean visualizar ) {
				
		if ( txtDtIni.getVlrDate() != null && txtDtFim.getVlrDate() != null ) {
			if ( txtDtFim.getVlrDate().before( txtDtIni.getVlrDate() ) ) {
				Funcoes.mensagemInforma( this, "Data final inferior a inicial!" );
				return;
			}
		}

		try {
			
			String nomevend = null;
			String razcli = null;
			String razfor = null;
			Date dtini = txtDtIni.getVlrDate();
			Date dtfim = txtDtFim.getVlrDate();
			
			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT " );
			sql.append( "  P.CODCLI, C.RAZCLI, IP.CODPROD, IP.REFPROD, PD.DESCPROD, " );
			sql.append( "  SUM(IP.QTDITPED) QUANTIDADE, SUM(IP.VLRLIQITPED) VLR_TOTAL, IP.PRECOITPED VLR_UNIT " );
			sql.append( "FROM " );
			sql.append( "  RPPEDIDO P, RPITPEDIDO IP, RPPRODUTO PD, RPCLIENTE C " );
			sql.append( "WHERE " );
			sql.append( "  P.CODEMP=IP.CODEMP AND P.CODFILIAL=IP.CODFILIAL AND P.CODPED=IP.CODPED AND " );
			sql.append( "  C.CODEMP=P.CODEMPCL AND C.CODFILIAL=P.CODFILIALCL AND C.CODCLI=P.CODCLI AND " );
			sql.append( "  IP.CODEMP=P.CODEMP AND IP.CODFILIAL=P.CODFILIAL AND " );                       
			sql.append( "  IP.CODEMPPD=PD.CODEMP AND IP.CODFILIALPD=PD.CODFILIAL AND IP.CODPROD=PD.CODPROD AND " );
			sql.append( "  IP.CODEMP=? AND IP.CODFILIAL=? AND" );
			sql.append( "  P.DATAPED BETWEEN ? AND ?" );
			
			if ( txtCodCli.getVlrString().trim().length() > 0 ) {
				sql.append( " AND P.CODCLI=" + txtCodCli.getVlrInteger() );
				razcli = txtRazCli.getVlrString();
			}
			if ( txtCodFor.getVlrString().trim().length() > 0 ) {
				sql.append( " AND P.CODFOR=" + txtCodFor.getVlrInteger() );
				razfor = txtRazFor.getVlrString();
			}
			if ( txtCodVend.getVlrString().trim().length() > 0 ) {
				sql.append( " AND P.CODVEND=" + txtCodVend.getVlrInteger().intValue() );
				nomevend = txtNomeVend.getVlrString();
			}
			
			sql.append( " GROUP BY P.CODCLI, IP.CODPROD, IP.REFPROD, C.RAZCLI, PD.DESCPROD, IP.PRECOITPED " );
			
			if ( "C".equals( rgOrdem.getVlrString() ) ) {
				sql.append( " ORDER BY P.CODCLI, IP.CODPROD, IP.REFPROD, C.RAZCLI, PD.DESCPROD, IP.PRECOITPED " );				
			}
			else if ( "R".equals( rgOrdem.getVlrString() ) ) {
				sql.append( " ORDER BY C.RAZCLI, IP.CODPROD, IP.REFPROD, P.CODCLI, PD.DESCPROD, IP.PRECOITPED " );				
			}
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPPEDIDO" ) );
			ps.setDate( 3, Funcoes.dateToSQLDate( dtini ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( dtfim ) );
			ResultSet rs = ps.executeQuery();
			
			HashMap<String,Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con );
			hParam.put( "DTINI", dtini );
			hParam.put( "DTFIM", dtfim );
			hParam.put( "NOMEVEND", nomevend );
			hParam.put( "RAZFOR", razfor );
			hParam.put( "RAZCLI", razcli );
			
			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/rpprodutocliente.jasper", "PRODUTO POR CLIENTE", null, rs, hParam, this );

			if ( visualizar ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}
			
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	public void setConexao( Connection cn ) {

		super.setConexao( cn );

		lcCliente.setConexao( cn );
		lcFornecedor.setConexao( cn );
		lcVendedor.setConexao( cn );
		
		prefere = RPPrefereGeral.getPrefere( cn );
	}

}
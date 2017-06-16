package pacoteBase.CONTROL;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import pacoteBase.VIEW.*;

public class ControlarAplicativo implements MouseListener, MouseMotionListener, KeyListener, ActionListener {

	private MontarPainelInicial pnCenario;
	private Graphics            desenhoCen, desenhoDir;
	private ControlarImagem     controleImagem;
	private String              nomeArquivoImagemDada;
	private char[][]            imagemCinza;
	private char[][]            imagemAtual;
	BufferedImage 				imagem;

	private int                 nLinImageAtual, nColImageAtual;
	private int                 nLinImageInic, nColImageInic;
	private int					nDimensaoFiltro;
	private boolean             estadoDesenho;

	//*******************************************************************************************
	public ControlarAplicativo( )
	{
		pnCenario = new MontarPainelInicial( this );
		pnCenario.showPanel();
		estadoDesenho  = false;
		nDimensaoFiltro = 3;
	}

	//*******************************************************************************************
	// METODO PARA CONTROLE DOS BOTOES DO APLICATIVO

	public void actionPerformed(ActionEvent e)
	{
		String comando, nomeArquivo;

		comando = e.getActionCommand();

		// DEFINE AMBIENTE GRAFICO
		if ( !estadoDesenho ) {
			pnCenario.iniciarGraphics();
			desenhoCen = pnCenario.getDesenhoC();
			desenhoDir = pnCenario.getDesenhoD();
		}

		// ENDS THE PROGRAM
		if(comando.equals("botaoFim")) {
			System.exit(0);	
		}

		// INICIA O PROGRAMA
		if(comando.equals("botaoImagem")) {

			// LE IMAGEM SOLICITADA
			nomeArquivoImagemDada = pnCenario.escolherArquivo ( 1 );
			if ( nomeArquivoImagemDada != null ) {
				controleImagem = new ControlarImagem( nomeArquivoImagemDada, desenhoCen );
				estadoDesenho  = true;
				imagemCinza    = controleImagem.getImagemCinza();
				imagem = controleImagem.getImagem();
				nLinImageInic  = controleImagem.getNLin();
				nColImageInic  = controleImagem.getNCol();

				pnCenario.mudarBotoes();
				pnCenario.limpaPainelDir( desenhoDir );
				controleImagem.mostrarImagemBuffer ( imagem, desenhoDir );

				nLinImageAtual = nLinImageInic;
				nColImageAtual = nColImageInic;
				imagemAtual    = controleImagem.copiarImagem ( imagemCinza, nLinImageInic, nColImageInic );
				
			}
		}

		if ( comando.equals( "botaoAcao3" ) ) {
			filtroCanny();
		}

		if ( comando.equals( "botaoAcao1" ) )  {
			filtroMediana();
		}

		if ( comando.equals( "botaoAcao4" ) ) {
			ruidoSalPimenta();
		}

		if ( comando.equals( "botaoAcao31" ) ) {
			nDimensaoFiltro = 3;
		}

		if ( comando.equals( "botaoAcao32" ) ) {
			nDimensaoFiltro = 5;
		}

		if ( comando.equals( "botaoAcao33" ) ) {
			nDimensaoFiltro = 7;
		}
		
		if ( comando.equals( "salvarlzw" ) ) {
			compressaoLZW();
		}
		
		if ( comando.equals( "abrirlzw" ) ) {
			descompressaoLZW();
		}

		if ( comando.equals( "botaoSalva" ) && estadoDesenho ) {
			nomeArquivo = pnCenario.escolherArquivo ( 2 );
			controleImagem.gravarImagem( nomeArquivo, imagemAtual, nLinImageAtual, nColImageAtual );
		}

		if ( comando.equals( "botaoReset" ) && estadoDesenho ) {
			pnCenario.limpaPainelCen( desenhoCen );
			controleImagem = new ControlarImagem( nomeArquivoImagemDada, desenhoCen );
			nLinImageAtual   = nLinImageInic;
			nColImageAtual   = nColImageInic;
			imagemAtual      = controleImagem.copiarImagem ( imagemCinza, nLinImageInic, nColImageInic );
			imagem = controleImagem.copia(controleImagem.getImagem() );

			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer ( imagem, desenhoDir );

			pnCenario.ativarPainelAcao1();
			pnCenario.resetaSistema();
		}
	}

	//*******************************************************************************************
	@Override
	public void keyPressed(KeyEvent e) {

	}

	//*******************************************************************************************
	@Override
	public void keyReleased(KeyEvent e) {

	}

	//*******************************************************************************************
	@Override
	public void keyTyped(KeyEvent e) {
		String caracteres = "0123456789.";

		if(!caracteres.contains(e.getKeyChar() + "" ) )
			e.consume();
	}	

	//*******************************************************************************************
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseReleased(MouseEvent e) {
		int x, y;

		x = (int) e.getX();
		y = (int) e.getY();
		
		pnCenario.setAcao4(controleImagem.gerCores(imagem, x, y) );

	}

	//*******************************************************************************************
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	//*******************************************************************************************
	private void filtroMediana()
	{
		try{
			int tamFiltro = pnCenario.getText31();
			if(tamFiltro % 2 == 1){
				imagem = controleImagem.mediana(imagem, tamFiltro);
				pnCenario.limpaPainelDir( desenhoDir );
				controleImagem.mostrarImagemBuffer(imagem, desenhoDir);
			}else{
				JOptionPane.showMessageDialog(null, "Filtro somente com tamanhos impares.", "Aviso", JOptionPane.WARNING_MESSAGE);
			}

		}catch(NumberFormatException e){
			imagem = controleImagem.mediana(imagem, nDimensaoFiltro);
			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer(imagem, desenhoDir);
		}


	}

	//*******************************************************************************************
	private void filtroCanny()
	{
		BufferedImage imgAux = controleImagem.getImagem();
		try {
			controleImagem.filtroCanny(imagem, pnCenario.getText11(), pnCenario.getText13(), pnCenario.getText12() );
			controleImagem.mesclarImagem(imgAux, imagem, 0);
			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer(imgAux, desenhoDir);
			imagem = imgAux;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Insira os parametros para filtro de canny", "Aviso", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}

	}

	//*******************************************************************************************
	private void ruidoSalPimenta()
	{
		try {
			controleImagem.ruido(imagem, 255, 255, 255, pnCenario.getText21() );
			controleImagem.ruido(imagem, 0, 0, 0, pnCenario.getText22() );
			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer(imagem, desenhoDir);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Insira os parametros para Ruido Sal e Pimenta", "Aviso", JOptionPane.WARNING_MESSAGE);
		}

	}

	//*******************************************************************************************
	
	private void compressaoLZW(){
		String nomeArquivo = pnCenario.escolherArquivo ( 2 );
		controleImagem.compressaoLZW(nomeArquivo, imagem);
	}
	
	//*******************************************************************************************
	
	private void descompressaoLZW(){
		BufferedImage img;
		String nomeArquivo = pnCenario.escolherArquivo ( 1 );
		
		if ( nomeArquivo != null ) {
			img = controleImagem.descompressaoLZW(nomeArquivo);
			pnCenario.limpaPainelDir( desenhoDir );
			controleImagem.mostrarImagemBuffer(img, desenhoDir);
		}
	}


}

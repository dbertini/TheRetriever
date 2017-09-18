/**
 * 
 */
package it.db.retriever.sender;

/**
 * Interfaccia da implementare per creare oggetti
 * in grado in inviare i report creati
 * 
 * @author dbertini
 *
 */
public interface ReportSenderInterface {
	
	/**
	 * Metodo per l'invio di un messaggio con un file allegato
	 * 
	 * @param aSubject subject della mail
	 * @param aHTMLMessage Messaggio in HTML della mail
	 * @param aRecipients lista dei destinatari separati da ;
	 * @param aCcList lista dei destinatari cc separati da ;
	 * @param aCcnList lista dei destinatari ccn separati da ;
	 * @param aNameFileAttachment percorso e nome del file da allegare
	 */
	public abstract void sendMailWithAttachments(String aSubject, String aHTMLMessage, String aRecipients, String aCcList, String aCcnList, String aNameFileAttachment);

}

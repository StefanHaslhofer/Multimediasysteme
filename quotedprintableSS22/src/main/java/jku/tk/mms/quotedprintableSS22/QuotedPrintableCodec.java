package jku.tk.mms.quotedprintableSS22;

public class QuotedPrintableCodec {
	public static final char	DEFAULT_QUOTE_CHAR	= '=';
	
	public QuotedPrintableCodec() {
		this(DEFAULT_QUOTE_CHAR);
	}
	
	public QuotedPrintableCodec(char quoteChar) {
	}
	
	public String encode(String plain) {
		StringBuffer quoted = new StringBuffer();
		
/*     @TODO Place your implementation here             */
		
		return quoted.toString();
	}
	
	public String decode(String quoted) {
		StringBuffer plain = new StringBuffer();

/*     @TODO Place your implementation here             */
		
		return plain.toString();
	}
}

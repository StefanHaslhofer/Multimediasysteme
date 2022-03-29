package jku.tk.mms.quotedprintableSS22;

public class QuotedPrintableCodec {
    public static final char DEFAULT_QUOTE_CHAR = '=';
	private final char quoteChar;

    public QuotedPrintableCodec() {
        this(DEFAULT_QUOTE_CHAR);
    }

    public QuotedPrintableCodec(char quoteChar) {
		this.quoteChar = quoteChar;
    }

    public String encode(String plain) {
        StringBuffer quoted = new StringBuffer();

        for (char c : plain.toCharArray()) {
            // true if char can be transmitted without encoding, false otherwise
            if (c == 9 || 32 <= c && c <= 60 || 62 <= c && c <= 126) {
                quoted.append(c);
            } else {
                quoted.append(this.quoteChar).append(Integer.toHexString(c).toUpperCase());
            }
        }

        return quoted.toString();
    }

    public String decode(String quoted) {
        StringBuffer plain = new StringBuffer();

        char[] charArr = quoted.toCharArray();
		for (int i = 0; i< charArr.length; i++) {
            // '=' indicates start of an encoded character transmission
			if (charArr[i] == '=') {
                // we need to append "0x" in front of the string in order to match the hex format
                plain.append((char)Integer.decode("0x" + charArr[i+1] + charArr[i+2]).intValue());
                i+=2;
			} else {
                plain.append(charArr[i]);
			}
		}

        return plain.toString();
    }
}

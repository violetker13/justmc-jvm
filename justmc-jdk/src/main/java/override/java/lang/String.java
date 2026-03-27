package override.java.lang;

import justmc.Text;
import justmc.Unsafe;
import justmc.annotation.Inline;

@Inline
public final class String implements CharSequence {

    private final Text value;

    public String() {
        this.value = Text.plain("");
    }

    public String(java.lang.String original) {
        this.value = Unsafe.<String>cast(original).value;
    }

    public String(Text value) {
        this.value = value;
    }

    public java.lang.String toString() {
        return Unsafe.cast(this);
    }

    public int length() {
        return value.getLength();
    }

    // TODO сделать такие же методы в justmc/Text и просто их вызвать тут (так же, как length выше)
//    @Override
//    public boolean isEmpty() {

//    }

    @Override
    public char charAt(int index) {
        return 0;
    }

//    public boolean equalsIgnoreCase(String anotherString) {

//    }

//    public boolean startsWith(String prefix) {

//    }

//    public boolean endsWith(String suffix) {

//    }

//    public int indexOf(int ch) {
//
//    }

//    public int indexOf(int ch, int fromIndex) {

//    }

//    public int indexOf(int ch, int beginIndex, int endIndex) {

//    }
//
//    public int lastIndexOf(int ch) {

//    }
//
//    public int lastIndexOf(int ch, int fromIndex) {

//    }
//
//    public int indexOf(String str) {
//
//    }
//
//    public int indexOf(String str, int fromIndex) {
//
//    }
//
//    public int indexOf(String str, int beginIndex, int endIndex) {
//
//    }
//
//    public int lastIndexOf(String str) {
//
//    }
//
//    public int lastIndexOf(String str, int fromIndex) {
//
//    }
//
//    public String substring(int beginIndex) {
//
//    }
//
//    public String substring(int beginIndex, int endIndex) {

//    }
//

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return null;
    }
//
//    public String concat(String str) {
//
//    }
//
//    public String replace(char oldChar, char newChar) {
//
//    }
//
//    public boolean matches(String regex) {
//        return Pattern.matches(regex, this);
//    }
//
//    public boolean contains(CharSequence s) {

//    }
//
//    public String replaceFirst(String regex, String replacement) {
//
//    }
//
//    public String replaceAll(String regex, String replacement) {
//
//    }
//
//    public String replace(CharSequence target, CharSequence replacement) {
//
//    }

//    public String[] split(String regex, int limit) {
//
//    }
//
//    public String[] splitWithDelimiters(String regex, int limit) {
//
//    }
//
//    public String[] split(String regex) {
//
//    }


//
//    public String toLowerCase() {

//    }
//

//
//    public String toUpperCase() {

//    }
//
//    public String trim() {

//    }
//
//    public String strip() {

//    }
//
//    public String stripLeading() {

//    }
//
//    public String stripTrailing() {

//    }
//
//    public String stripIndent() {

//    }

    public static java.lang.String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    public static java.lang.String valueOf(boolean b) {
        return b ? "true" : "false";
    }

//    public static java.lang.String valueOf(char c) {
//        return Unsafe.asPrimitive(c);
//    }

    public static java.lang.String valueOf(int i) {
        return Integer.toString(i);
    }

    public static java.lang.String valueOf(long l) {
        return Long.toString(l);
    }

    public static java.lang.String valueOf(float f) {
        return Float.toString(f);
    }

    public static java.lang.String valueOf(double d) {
        return Double.toString(d);
    }

//    public java.lang.String repeat(int count) {
//
//    }

}

package hi.event.vinnsla;
/******************************************************************************
 *  Nafn    : Ásdís Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *  Lýsing  : Vinnsluklasi fyrir flokk af viðburðum
 *
 *
 *****************************************************************************/
public enum Group {

    EDUCATION("Education"),
    ENTERTAINMENT("Entertainment"),
    FAMILY("Family");

    private final String name;

    Group(String name) {
        this.name = name;
    }

    /**
     * skilar nafni flokksins
     * @return nafn flokksins
     */
    public String toString() {
        return name;
    }
}

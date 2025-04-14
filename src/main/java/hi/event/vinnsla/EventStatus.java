package hi.event.vinnsla;
/******************************************************************************
 *  Nafn    : Ásdís Stefánsdóttir
 *  T-póstur: ahl4@hi.is
 *  Lýsing  : Vinnsluklasi fyrir status af viðburðum
 *
 *
 *****************************************************************************/

public enum EventStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    CANCELLED("Cancelled");

    private final String statusName;

    EventStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    @Override
    public String toString() {
        return statusName;
    }
}

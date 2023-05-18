public abstract class CheckListItemAbstract {

    private boolean isSelected = false;

    public CheckListItemAbstract() {}

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "CheckListItem";
    }
}
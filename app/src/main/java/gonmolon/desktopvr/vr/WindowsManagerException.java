package gonmolon.desktopvr.vr;

public class WindowsManagerException extends Exception {

    private Error error;

    public WindowsManagerException(Error error) {
        super();
        this.error = error;
    }

    public Error getError() {
        return error;
    }

    @Override
    public void printStackTrace() {
        if(error == Error.ID_INVALID) {
            System.out.println("ID invalid. Maybe last id is greater?");
        } else if(error == Error.ID_NONEXISTENT) {
            System.out.println("ID not exists. Element doesn't exist with this id");
        }  else if(error == Error.ID_USED) {
            System.out.println("ID used");
        }
    }

    public enum Error {
        ID_INVALID, ID_USED, ID_NONEXISTENT
    }
}

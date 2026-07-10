package recuperacion.ConcaFlores.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Integer id) {
        super(resource + " con id " + id + " no encontrado.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;

public enum Command {

    LIST() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("LIST") || commands[0].startsWith("list");
        }

        @Override
        Path execute(Path path) throws IOException {

            if (!Files.exists(path) || !Files.isDirectory(path)) {
                throw new UnsupportedOperationException("Diretório atual inválido.");
            }

            try (var stream = Files.list(path)) {
                stream.forEach(p -> {
                    if (Files.isDirectory(p)) {
                        System.out.println(p.getFileName() + File.separator);
                    }
                    else {
                        System.out.println(p.getFileName());
                    }
                });
            }

            return path;
        }
    },
    SHOW() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("SHOW") || commands[0].startsWith("show");
        }

        @Override
        Path execute(Path path) {

            if (parameters.length < 2) {
                throw new UnsupportedOperationException("Informe o arquivo. Ex: SHOW arquivo.txt");
            }

            final var fileName = parameters[1];
            final var target = path.resolve(fileName);

            if (!Files.exists(target)) {
                throw new UnsupportedOperationException("Arquivo não encontrado.");
            }

            if (Files.isDirectory(target)) {
                throw new UnsupportedOperationException("SHOW não pode ser usado em diretórios.");
            }

            if (!target.getFileName().toString().toLowerCase().endsWith(".txt")) {
                throw new UnsupportedOperationException("SHOW deve ser usado apenas em arquivos TXT.");
            }

            new FileReader().read(target);

            return path;
        }
    },
    BACK() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("BACK") || commands[0].startsWith("back");
        }

        @Override
        Path execute(Path path) {

            final var root = Paths.get(Application.ROOT).normalize().toAbsolutePath();
            final var current = path.normalize().toAbsolutePath();

            if (current.equals(root)) {
                throw new UnsupportedOperationException("Não tem como ir além do diretório raiz.");
            }

            final var parent = current.getParent();

            if (parent == null) {
                throw new UnsupportedOperationException("Não tem como ir além do diretório raiz.");
            }

            return path;
        }
    },
    OPEN() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("OPEN") || commands[0].startsWith("open");
        }

        @Override
        Path execute(Path path) {

            if (parameters.length < 2) {
                throw new UnsupportedOperationException("Informe o diretório. Ex: OPEN pasta");
            }

            final var dirName = parameters[1];
            final var target = path.resolve(dirName);

            if (!Files.exists(target)) {
                throw new UnsupportedOperationException("Diretório não encontrado.");
            }

            if (!Files.isDirectory(target)) {
                throw new UnsupportedOperationException("OPEN só pode ser usado com diretórios.");
            }

            return path;
        }
    },
    DETAIL() {
        private String[] parameters = new String[]{};

        @Override
        void setParameters(String[] parameters) {
            this.parameters = parameters;
        }

        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("DETAIL") || commands[0].startsWith("detail");
        }

        @Override
        Path execute(Path path) {

            if (parameters.length < 2) {
                throw new UnsupportedOperationException("Informe o arquivo ou diretório. Ex: DETAIL nome");
            }

            final var name = parameters[1];
            final var target = path.resolve(name);

            if (!Files.exists(target)) {
                throw new UnsupportedOperationException("Arquivo/Diretório não encontrado.");
            }

            final var view = Files.getFileAttributeView(target, BasicFileAttributeView.class);
            if (view == null) {
                throw new UnsupportedOperationException("Não foi possível obter atributos do arquivo/diretório.");
            }

            try {
                final var attrs = view.readAttributes();

                System.out.println("Nome: " + target.getFileName());
                System.out.println("Caminho: " + target.toAbsolutePath());
                System.out.println("Diretório: " + attrs.isDirectory());
                System.out.println("Arquivo regular: " + attrs.isRegularFile());
                System.out.println("Tamanho: " + attrs.size() + " bytes");
                System.out.println("Criado em: " + attrs.creationTime());
                System.out.println("Último acesso: " + attrs.lastAccessTime());
                System.out.println("Última modificação: " + attrs.lastModifiedTime());

            }

            catch (IOException e) {
                throw new UnsupportedOperationException("Erro ao ler atributos do arquivo/diretório.");
            }

            return path;
        }
    },
    EXIT() {
        @Override
        boolean accept(String command) {
            final var commands = command.split(" ");
            return commands.length > 0 && commands[0].startsWith("EXIT") || commands[0].startsWith("exit");
        }

        @Override
        Path execute(Path path) {
            System.out.print("Saindo...");
            return path;
        }

        @Override
        boolean shouldStop() {
            return true;
        }
    };

    abstract Path execute(Path path) throws IOException;

    abstract boolean accept(String command);

    void setParameters(String[] parameters) {
    }

    boolean shouldStop() {
        return false;
    }

    public static Command parseCommand(String commandToParse) {

        if (commandToParse.isBlank()) {
            throw new UnsupportedOperationException("Type something...");
        }

        final var possibleCommands = values();

        for (Command possibleCommand : possibleCommands) {
            if (possibleCommand.accept(commandToParse)) {
                possibleCommand.setParameters(commandToParse.split(" "));
                return possibleCommand;
            }
        }

        throw new UnsupportedOperationException("Can't parse command [%s]".formatted(commandToParse));
    }
}

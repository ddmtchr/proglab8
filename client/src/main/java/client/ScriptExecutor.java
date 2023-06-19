package client;

import exceptions.*;
import input.CommandInputReceiver;
import utility.Request;
import utility.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

public class ScriptExecutor {
    private final ArrayList<String> scriptStack = new ArrayList<>();
    private final StringBuilder scriptResponse = new StringBuilder();
    public int executeScript(File scriptFile, ConnectionProvider connectionProvider, Session session) throws ErrorInScriptException {
        int execCode;
        int execCommandCode = 0;
        try {
            Request request;
            scriptStack.add(scriptFile.getName());
            if (!scriptFile.exists()) throw new FileNotFoundException();
            if (!scriptFile.canRead()) throw new SecurityException();
            Scanner scriptScanner = new Scanner(scriptFile);
            if (!scriptScanner.hasNextLine()) throw new EmptyScriptException();
            CommandInputReceiver cir = new CommandInputReceiver(scriptScanner, scriptFile.getName());
            cir.getFir().setFileReadMode(true);
            System.out.println("Executing script " + scriptFile.getName() + "...");
            do {
                String[] command = cir.parseCommand();
                if (command[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (command[1].equals(script)) {
                            cir.getFir().setFileReadMode(false);
                            throw new ScriptRecursionException();
                        }
                    }
                    System.out.println(String.join(" ", command));
                    execCommandCode = executeScript(new File(command[1]), connectionProvider, session);

                } else if (command[0].equals("exit")) {
                    if (command[1].equals(" ")) {
                        System.out.println("Выход из программы...");
                        System.exit(0);
                    } else throw new ErrorInScriptException();
                }
                if (execCommandCode == 2) throw new RecursionException();

                System.out.println(String.join(" ", command));
                request = cir.pack(command, session);
                connectionProvider.send(request);
                Response response = connectionProvider.receive();

                if (response == null) {
                    execCommandCode = 3;
                } else {
                    System.out.println(response.getBody().getText());
                    execCommandCode = response.getExecCode();
                }

            } while (scriptScanner.hasNextLine() && execCommandCode == 0);
            cir.getFir().setFileReadMode(false);
            if (execCommandCode == 1) throw new ErrorInScriptException();
            if (execCommandCode == 3) throw new ResponseReceivingException();
            execCode = 0;
            appendln("Script " + scriptFile.getName() + " executed successfully!");

        } catch (FileNotFoundException e) {
            execCode = 4;
            appendln("File not found");
        } catch (SecurityException e) {
            execCode = 4;
            appendln("No permission to read the file");
        } catch (EmptyScriptException e) {
            execCode = 1;
            appendln("The script file is empty");
        } catch (ScriptRecursionException e) {
            execCode = 2;
            appendln("Recursive call detected in the script");
        } catch (NoSuchCommandException e) {
            throw new ErrorInScriptException();
        } catch (RecursionException e) {
            execCode = 2;
        } catch (ResponseReceivingException | SocketTimeoutException e) {
            execCode = 3;
            appendln("We apologize, the server has moved the horses");
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return execCode;
    }

    public void appendln(String s) {
        scriptResponse.append(s).append("\n");
    }

    public String getAndClear() {
        String responseToGet = scriptResponse.toString();
        scriptResponse.setLength(0);
        return responseToGet;
    }
}

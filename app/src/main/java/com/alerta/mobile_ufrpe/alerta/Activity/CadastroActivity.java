package com.alerta.mobile_ufrpe.alerta.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alerta.mobile_ufrpe.alerta.DAO.ConfiguracaoFirebase;
import com.alerta.mobile_ufrpe.alerta.Entidades.Usuarios;
import com.alerta.mobile_ufrpe.alerta.Helper.Base64Custom;
import com.alerta.mobile_ufrpe.alerta.Helper.Preferencias;
import com.alerta.mobile_ufrpe.alerta.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroActivity extends AppCompatActivity {

    private Usuarios usuarios;
    private FirebaseAuth autenticacao;

    private EditText edtCadEmail;
    private EditText edtCadSenha;
    private EditText edtCadConfSenha;
    private EditText edtCadNome;

    private Button btnGravar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        edtCadEmail = (EditText) findViewById(R.id.edtCadEmail);
        edtCadSenha = (EditText) findViewById(R.id.edtCadSenha);
        edtCadConfSenha = (EditText) findViewById(R.id.edtCadConfSenha);
        edtCadNome = (EditText) findViewById(R.id.edtCadNome);

        btnGravar = (Button) findViewById(R.id.btnGravar);

        //Evento no Botão Gravar
        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtCadSenha.getText().toString().equals(edtCadConfSenha.getText().toString())) {

                    usuarios = new Usuarios();
                    usuarios.setEmail(edtCadEmail.getText().toString());
                    usuarios.setSenha(edtCadSenha.getText().toString());
                    usuarios.setConfSenha(edtCadConfSenha.getText().toString());
                    usuarios.setNome(edtCadNome.getText().toString());

                    cadastrarUsuario();

                } else {
                    Toast.makeText(CadastroActivity.this, "As senhas não são correspondentes", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()

        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificadorUsuario);
                    usuarios.salvar();

                    Preferencias preferencias = new Preferencias(CadastroActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, usuarios.getNome());

                    abrirLoginUsuario();

                } else {
                    String errorExcecao; // Tratamento das exceções do cadastro
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        errorExcecao = "Digite uma senha mais forte contendo no mínimo 8 caracteres de letras e números";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        errorExcecao = "O e-mail digitado não é valido, digite um novo e-mail";
                    }catch (FirebaseAuthUserCollisionException e) {
                        errorExcecao = "Esse e-mail já esta cadastrado no sistema";
                    }catch (Exception e) {
                        errorExcecao = "Erro ao efetuar cadasto";
                         }
                    Toast.makeText(CadastroActivity.this, "Error " + errorExcecao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Metodo que abre tela de login
    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

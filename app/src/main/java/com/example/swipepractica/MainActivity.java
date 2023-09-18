package com.example.swipepractica;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LinearLayout container;
    private Button changeColorButton;
    private TextView resultadoTextView;
    private int cantidadTotal = 0;
    private boolean isWhiteToBlack = true;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.container);
        changeColorButton = findViewById(R.id.changeColorButton);
        resultadoTextView = findViewById(R.id.resultadoTextView);

        // Inicializa el GestureDetector
        gestureDetector = new GestureDetector(this, new SwipeGestureListener());

        changeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBackgroundColor();
            }
        });

        // Asigna el Listener de toque al TextView para detectar gestos
        resultadoTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    private void toggleBackgroundColor() {
        int startColor, endColor;

        if (isWhiteToBlack) {
            startColor = Color.WHITE;
            endColor = Color.BLACK;
        } else {
            startColor = Color.BLACK;
            endColor = Color.WHITE;
        }

        container.setBackgroundColor(startColor);
        container.setBackgroundResource(R.drawable.gradient_background);

        isWhiteToBlack = !isWhiteToBlack;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            // Incrementar la cantidad total
            cantidadTotal++;

            // Calcular billetes y monedas
            int billetes100 = cantidadTotal / 100;
            int billetes50 = (cantidadTotal % 100) / 50;
            int billetes20 = ((cantidadTotal % 100) % 50) / 20;
            int monedas10 = (((cantidadTotal % 100) % 50) % 20) / 10;
            int monedas5 = ((((cantidadTotal % 100) % 50) % 20) % 10) / 5;

            // Mostrar los resultados en el TextView
            String resultado = "Billetes de 100: " + billetes100 + "\n" +
                    "Billetes de 50: " + billetes50 + "\n" +
                    "Billetes de 20: " + billetes20 + "\n" +
                    "Monedas de 10 centavos: " + monedas10 + "\n" +
                    "Monedas de 5 centavos: " + monedas5;

            resultadoTextView.setText(resultado);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Detectar un gesto de deslizamiento hacia abajo
            if (e1.getY() - e2.getY() > 50 && Math.abs(velocityY) > 200) {
                // Limpiar el TextView
                resultadoTextView.setText("");
                return true;
            }
            // Detectar un gesto de deslizamiento hacia arriba
            if (e2.getY() - e1.getY() > 50 && Math.abs(velocityY) > 200) {
                // Reiniciar la aplicación
                restartApp();
                return true;
            }
            return false;
        }
    }

    private void restartApp() {
        // Reiniciar la aplicación volviendo a cargar la actividad principal
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
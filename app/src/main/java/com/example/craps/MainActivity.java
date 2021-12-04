package com.example.craps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.security.KeyPairGenerator;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public Button button;
    ImageView user1, user2, user3, user4, house1, house2, house3, house4;

    TextView userscore, gameMessage, bet, avail, currentBet;

    Random r;

    EditText editText;
    /* creating the integers that i will be using and animation */
    int rollCounter = 0;
    int userPoints = 0 , housePoints = 0;
    int userPoints1 = 0, housePoints1 = 0;
    int counter = 0, counter1 = 0;
    int totalcount = 0;
    int pointtowin = 0;
    int pointtowin1 =0;
    int playerBet = 5;
    int total = 0;
    int userpoints;
    int housepoints;
    int totalHouse;
    LinearLayout playButtons;
    GridLayout betButtons;
    private AdView mAdView;
    Animation animation;


    @Override
    /*setting up the text view and image view */
    protected void onStart() {
        super.onStart();
        TextView avail1 = (TextView) findViewById(R.id.bank);
        avail1.setText("Place a bet (You have $" + total + ")");
    }
    protected void onCreate(Bundle savedInstanceState)
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        String totals = getIntent().getStringExtra("et");

        try{
            int total1 = Integer.parseInt(totals);
            total = total1;

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        avail = (TextView) findViewById(R.id.bank);



        // user cards
        user1 = (ImageView) findViewById(R.id.user1);
        user2 = (ImageView) findViewById(R.id.user2);
        user3 = (ImageView) findViewById(R.id.user3);
        user4 = (ImageView) findViewById(R.id.user4);

        // house cards
        house1 = (ImageView) findViewById(R.id.house1);
        house2 = (ImageView) findViewById(R.id.house2);
        house3 = (ImageView) findViewById(R.id.house3);
        house4 = (ImageView) findViewById(R.id.house4);

        // play buttons
        playButtons = findViewById(R.id.play_buttons);
        // bet buttons
        betButtons = findViewById(R.id.bet_buttons);

        // current bet
        currentBet = (TextView) findViewById(R.id.current_bet);


        userscore = (TextView) findViewById(R.id.player_score);
        gameMessage = (TextView) findViewById(R.id.game_message);


        /*button = (Button) findViewById(R.id.reset_button);
        button.setOnClickListener(new View.OnClickListener() {

            //Resets variable values to default and returns to home screen
            @Override
            public void onClick(View view) {
                housepoints = 0;
                gameMessage.setText("Most casinos consider that cheating...");
                userpoints = 0;
                userscore.setText("User: " + userpoints);
                avail.setText("Bank: $" + total);
                Intent intent=new Intent (MainActivity.this,Home.class);
                startActivity(intent);
                String value = button.getText().toString();

            }
        });*/

        r = new Random();

        animation = AnimationUtils.loadAnimation(this, R.anim.rotate);

    }

    int card1, card2, card3, card4, hit1, hit2;



    // stand function
    public void stand(View view) {
        setCard(card4, house2); // flip house card 2
        totalHouse += counter;

        // compare cards to determine winner
        if((21-totalcount) > (21-totalHouse)) {
            // user wins
            gameMessage.setText("Dealer: " + totalHouse + "\nYou win!");
            updateBank("win"); // update bank
        }
        else if((21-totalcount) == (21-totalHouse)) {
            // no winner
            gameMessage.setText("Dealer: " + totalHouse + "\nDraw.");
            updateBank("draw");
        }
        else {
            //user loses
            gameMessage.setText("House: " + totalHouse + "\nYou lose!");
            updateBank("lose");
        }
        // reset roll counter
        rollCounter = 0;
    }

    // reset button
    public void resetGame(View view) {
        Intent i =new Intent (MainActivity.this,Home.class);
        startActivity(i);
    }

    // this function replaces the OnClickListener for "rolls" (hit)
    public void hitMe(View view) {
        if(rollCounter == 0) {
            // hide bet buttons
            betButtons.setVisibility(View.GONE);
            TextView avail1 = (TextView) findViewById(R.id.bank);
            avail1.setText("Bank: $" + total);

            user1.setImageResource(R.drawable.cardback);
            user2.setImageResource(R.drawable.cardback);
            house1.setImageResource(R.drawable.cardback);
            house2.setImageResource(R.drawable.cardback);
            hideCard(user3);
            hideCard(user4);
            // current bet
            currentBet.setText("Current Bet: $" + playerBet);
            currentBet.setVisibility(View.VISIBLE);
        }
        rollCounter++;
        //int card1, card2, card3, card4, card5, card6;

        // initialize cards
        if (rollCounter == 1) {
            card1 = r.nextInt(52) + 1;
            // get card 2 value, check against card 1
            do{
                card2 = r.nextInt(52) + 1;
            } while(card1 == card2);
            // get card 3 value, check against card 1
            do{
                card3 = r.nextInt(52) + 1;
            } while(card3 == card1 || card3 == card2);
            // get card 4 value, check against card 1
            do{
                card4 = r.nextInt(52) + 1;
            } while(card4 == card1 || card4 == card2 || card4 == card3);

            setCard(card1, user1);
            totalcount = counter;
            setCard(card2, user2);
            totalcount += counter;
            setCard(card3, house1);
            totalHouse = counter;

        }
        // when appropriate, unhide cards 3 and 4 if user selects "hit"
        // hit 1
        else if (rollCounter == 2) {
            // get card value
            do{
                hit1 = r.nextInt(52) + 1;
            } while(hit1 == card1 || hit1 == card2 || hit1 == card3 || hit1 == card4);

            setCard(hit1, user3); // set value of card 3
            totalcount += counter;
            showCard(user3); // show card
        }
        // hit 2
        else if (rollCounter == 3) {
            // get card value
            do{
                hit2 = r.nextInt(52) + 1;
            } while(hit1 == card1 || hit1 == card2 || hit1 == card3 || hit1 == card4 || hit1 == hit2);
            setCard(hit2, user4); // set value of card 4
            totalcount += counter;
            showCard(user4); // show card
        }


        // update this
        // if user has chips available
        if (total > 0) {

                userscore.setText("Your Hand: " + totalcount);
        }

        if (total == 0) {
            TextView avail1 = (TextView) findViewById(R.id.bank);
            avail1.setText("Sorry you have no more money left please deposit more to keep playing");
        }


        // if new card results in a score of 21 or greater, game ends
        if(totalcount >= 21) {
            if(totalcount == 21) {
                setCard(card4, house2); // flip house card 2
                totalHouse += counter;
                // if player has 21
                if (totalHouse == 21) {
                    // if dealer has 21, game is a draw
                    gameMessage.setText("Draw.");
                    updateBank("draw");
                } else {
                    // player wins
                    gameMessage.setText("You win!");
                    updateBank("win");
                }
            }
            // if player hand exceeds 21, game is a loss
            else {
                gameMessage.setText("You lose!");
                updateBank("lose");
            }

            // reset roll counter
            rollCounter = 0;
        }
    }

    public void incBet(View view){
        // if attempted bet exceeds bank
        if((playerBet + 5) > total){
            Toast.makeText(MainActivity.this, "Check your pockets, high roller.", Toast.LENGTH_SHORT).show();
        }
        // if new bet is okay
        else {
            playerBet += 5;

            TextView betammount1 = (TextView) findViewById(R.id.bet);
            betammount1.setText("$"+playerBet);
        }

    }

    public void decBet(View view){
        // if player tries to bet less than 5
        if((playerBet - 5) <= 0) {
            Toast.makeText(MainActivity.this, "Minimum bet is $5.", Toast.LENGTH_SHORT).show();
        }
        // update if bet is okay
        else {
            playerBet -= 5;
            TextView betammount1 = (TextView) findViewById(R.id.bet);
            betammount1.setText("$"+playerBet);
        }
    }

    public void updateBank(String outcome){
        currentBet.setVisibility(View.GONE);
        switch(outcome) {
            case "win":
                total += playerBet;
                // show bet buttons
                betButtons.setVisibility(View.VISIBLE);
                TextView avail1 = (TextView) findViewById(R.id.bank);
                avail1.setText("Place a bet (You have $" + total + ")");
                break;
            case "lose":
                total -= playerBet;
                if (total <= 5) {
                    gameMessage.setText("Add funds to continue.");
                    // hide play buttons
                    playButtons.setVisibility(View.INVISIBLE);
                } else {
                    // show bet buttons
                    betButtons.setVisibility(View.VISIBLE);
                    avail1 = (TextView) findViewById(R.id.bank);
                    avail1.setText("Place a bet (You have $" + total + ")");
                }
                break;
            case "draw":
                // show bet buttons
                betButtons.setVisibility(View.VISIBLE);
                avail1 = (TextView) findViewById(R.id.bank);
                avail1.setText("Place a bet (You have $" + total + ")");
                break;
        }
    }

        /*if(5 <= playerBet && playerBet <= total){
            playerBet = playerBet - 5;

            TextView betammount1 = (TextView) findViewById(R.id.bet);
            betammount1.setText("$"+playerBet);

        }
        if(playerBet > total){
            Toast.makeText(MainActivity.this, "You don't have enough", Toast.LENGTH_SHORT).show();

        }
        if(playerBet < 5){
            Toast.makeText(MainActivity.this, "You cannot bet an amount less than 5.", Toast.LENGTH_SHORT).show();

        }

    }*/

    // set card resource image and value
    public void setCard(int num, ImageView cardName){
        switch(num){
            case 1:
                counter = 1;
                cardName.setImageResource(R.drawable.c1);
                cardName.startAnimation(animation);
                break;
            case 2:
                counter = 2;
                cardName.setImageResource(R.drawable.c2);
                cardName.startAnimation(animation);
                break;
            case 3:
                counter = 3;
                cardName.setImageResource(R.drawable.c3);
                cardName.startAnimation(animation);
                break;
            case 4:
                counter = 4;
                cardName.setImageResource(R.drawable.c4);
                cardName.startAnimation(animation);
                break;
            case 5:
                counter = 5;
                cardName.setImageResource(R.drawable.c5);
                cardName.startAnimation(animation);
                break;
            case 6:
                counter = 6;
                cardName.setImageResource(R.drawable.c6);
                cardName.startAnimation(animation);
                break;
            case 7:
                counter = 7;
                cardName.setImageResource(R.drawable.c7);
                cardName.startAnimation(animation);
                break;
            case 8:
                counter = 8;
                cardName.setImageResource(R.drawable.c8);
                cardName.startAnimation(animation);
                break;
            case 9:
                counter = 9;
                cardName.setImageResource(R.drawable.c9);
                cardName.startAnimation(animation);
                break;
            case 10:
                counter = 10;
                cardName.setImageResource(R.drawable.c10);
                cardName.startAnimation(animation);
                break;
            case 11:
                counter = 10;
                cardName.setImageResource(R.drawable.c11);
                cardName.startAnimation(animation);
                break;
            case 12:
                counter = 10;
                cardName.setImageResource(R.drawable.c12);
                cardName.startAnimation(animation);
                break;
            case 13:
                counter = 10;
                cardName.setImageResource(R.drawable.c13);
                cardName.startAnimation(animation);
                break;
            case 14:
                counter = 1;
                cardName.setImageResource(R.drawable.d1);
                cardName.startAnimation(animation);
                break;
            case 15:
                counter = 2;
                cardName.setImageResource(R.drawable.d2);
                cardName.startAnimation(animation);
                break;
            case 16:
                counter = 3;
                cardName.setImageResource(R.drawable.d3);
                cardName.startAnimation(animation);
                break;
            case 17:
                counter = 4;
                cardName.setImageResource(R.drawable.d4);
                cardName.startAnimation(animation);
                break;
            case 18:
                counter = 5;
                cardName.setImageResource(R.drawable.d5);
                cardName.startAnimation(animation);
                break;
            case 19:
                counter = 6;
                cardName.setImageResource(R.drawable.d6);
                cardName.startAnimation(animation);
                break;
            case 20:
                counter = 7;
                cardName.setImageResource(R.drawable.d7);
                cardName.startAnimation(animation);
                break;
            case 21:
                counter = 8;
                cardName.setImageResource(R.drawable.d8);
                cardName.startAnimation(animation);
                break;
            case 22:
                counter = 9;
                cardName.setImageResource(R.drawable.d9);
                cardName.startAnimation(animation);
                break;
            case 23:
                counter = 10;
                cardName.setImageResource(R.drawable.d10);
                cardName.startAnimation(animation);
                break;
            case 24:
                counter = 10;
                cardName.setImageResource(R.drawable.d11);
                cardName.startAnimation(animation);
                break;
            case 25:
                counter = 10;
                cardName.setImageResource(R.drawable.d12);
                cardName.startAnimation(animation);
                break;
            case 26:
                counter = 10;
                cardName.setImageResource(R.drawable.d13);
                cardName.startAnimation(animation);
                break;
            case 27:
                counter = 1;
                cardName.setImageResource(R.drawable.h1);
                cardName.startAnimation(animation);
                break;
            case 28:
                counter = 2;
                cardName.setImageResource(R.drawable.h2);
                cardName.startAnimation(animation);
                break;
            case 29:
                counter = 3;
                cardName.setImageResource(R.drawable.h3);
                cardName.startAnimation(animation);
                break;
            case 30:
                counter = 4;
                cardName.setImageResource(R.drawable.h4);
                cardName.startAnimation(animation);
                break;
            case 31:
                counter = 5;
                cardName.setImageResource(R.drawable.h5);
                cardName.startAnimation(animation);
                break;
            case 32:
                counter = 6;
                cardName.setImageResource(R.drawable.h6);
                cardName.startAnimation(animation);
                break;
            case 33:
                counter = 7;
                cardName.setImageResource(R.drawable.h7);
                cardName.startAnimation(animation);
                break;
            case 34:
                counter = 8;
                cardName.setImageResource(R.drawable.h8);
                cardName.startAnimation(animation);
                break;
            case 35:
                counter = 9;
                cardName.setImageResource(R.drawable.h9);
                cardName.startAnimation(animation);
                break;
            case 36:
                counter = 10;
                cardName.setImageResource(R.drawable.h10);
                cardName.startAnimation(animation);
                break;
            case 37:
                counter = 10;
                cardName.setImageResource(R.drawable.h11);
                cardName.startAnimation(animation);
                break;
            case 38:
                counter = 10;
                cardName.setImageResource(R.drawable.h12);
                cardName.startAnimation(animation);
                break;
            case 39:
                counter = 10;
                cardName.setImageResource(R.drawable.h13);
                cardName.startAnimation(animation);
                break;
            case 40:
                counter = 1;
                cardName.setImageResource(R.drawable.s1);
                cardName.startAnimation(animation);
                break;
            case 41:
                counter = 2;
                cardName.setImageResource(R.drawable.s2);
                cardName.startAnimation(animation);
                break;
            case 42:
                counter = 3;
                cardName.setImageResource(R.drawable.s3);
                cardName.startAnimation(animation);
                break;
            case 43:
                counter = 4;
                cardName.setImageResource(R.drawable.s4);
                cardName.startAnimation(animation);
                break;
            case 44:
                counter = 5;
                cardName.setImageResource(R.drawable.s5);
                cardName.startAnimation(animation);
                break;
            case 45:
                counter = 6;
                cardName.setImageResource(R.drawable.s6);
                cardName.startAnimation(animation);
                break;
            case 46:
                counter = 7;
                cardName.setImageResource(R.drawable.s7);
                cardName.startAnimation(animation);
                break;
            case 47:
                counter = 8;
                cardName.setImageResource(R.drawable.s8);
                cardName.startAnimation(animation);
                break;
            case 48:
                counter = 9;
                cardName.setImageResource(R.drawable.s9);
                cardName.startAnimation(animation);
                break;
            case 49:
                counter = 10;
                cardName.setImageResource(R.drawable.s10);
                cardName.startAnimation(animation);
                break;
            case 50:
                counter = 10;
                cardName.setImageResource(R.drawable.s11);
                cardName.startAnimation(animation);
                break;
            case 51:
                counter = 10;
                cardName.setImageResource(R.drawable.s12);
                cardName.startAnimation(animation);
                break;
            case 52:
                counter = 10;
                cardName.setImageResource(R.drawable.s13);
                cardName.startAnimation(animation);
                break;
        }
    }

    // this function is used to make cards 3 and 4 visible
    public void showCard(ImageView card) {
        card.setVisibility(View.VISIBLE);
    }

    // this function is used to hide cards
    public void hideCard(ImageView card) {
        card.setVisibility(View.GONE);
    }

}
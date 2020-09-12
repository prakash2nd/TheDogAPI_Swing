import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.TextArea;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class TheDogAPIswingForm {

	private JFrame frmTheDogApi;
	private JTextField dogName;
	private JLabel lblInformation;
	String dog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TheDogAPIswingForm window = new TheDogAPIswingForm();
					window.frmTheDogApi.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TheDogAPIswingForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTheDogApi = new JFrame();
		frmTheDogApi.setResizable(false);
		frmTheDogApi.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\praka\\Desktop\\cute-welsh-corgi-dog-waving-paw-cartoon_42750-623.jpg"));
		frmTheDogApi.setTitle("The Dog API");	//Title of Page
		frmTheDogApi.setBounds(100, 100, 528, 368);
		frmTheDogApi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTheDogApi.getContentPane().setLayout(null);

		JLabel lblDogInformation = new JLabel("Dog Information");
		lblDogInformation.setBounds(192, 11, 122, 27);
		frmTheDogApi.getContentPane().add(lblDogInformation);

		JLabel lblDogName = new JLabel("Dog Name");
		lblDogName.setBounds(74, 57, 61, 14);
		frmTheDogApi.getContentPane().add(lblDogName);

		dogName = new JTextField();	//to take input from User
		dogName.setBounds(145, 54, 209, 20);
		frmTheDogApi.getContentPane().add(dogName);
		dogName.setColumns(10);

		lblInformation = new JLabel("Information");	//to display the fetched information
		lblInformation.setBounds(20, 123, 72, 14);
		frmTheDogApi.getContentPane().add(lblInformation);

		TextArea information = new TextArea();
		information.setEditable(false);
		information.setBounds(20, 141, 482, 178);
		frmTheDogApi.getContentPane().add(information);

		Button submit = new Button("Submit");	//submit information to fetch the information
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if ((dogName.getText().isEmpty()))
					JOptionPane.showMessageDialog(null, "Data Missing");
				else
					dog = dogName.getText();
				try {
					MyGETRequest(dog, information); //fetching the information of dog
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		submit.setBounds(145, 92, 70, 22);
		frmTheDogApi.getContentPane().add(submit);

		Button reset = new Button("Reset");	//clear the form
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dogName.setText(null);
				information.setText(null);
			}
		});
		reset.setBounds(284, 92, 70, 22);
		frmTheDogApi.getContentPane().add(reset);
	}

	/**
	 * fetching the data and displaying the same in text area on the form
	 * @param dogName
	 * @param information
	 * @throws IOException
	 */
	public static void MyGETRequest(String dogName, TextArea information) throws IOException {
		URL urlForGetRequest = new URL("https://api.thedogapi.com/v1/breeds");
		String readLine = null;
		HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
		conection.setRequestMethod("GET");
		int responseCode = conection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();

			String responseString = response.toString();
			JSONArray jsonArray = new JSONArray(responseString);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);

				if (jsonObject.get("name").equals(dogName)) {

					information.append("Name: " + jsonObject.get("name").toString());

					if (jsonObject.has("description") && !jsonObject.get("description").equals("")) {
						information.append("Description: " + jsonObject.get("description").toString());
					}

					information.append("\nWeight: (" + jsonObject.getJSONObject("weight").get("metric") + ")KG OR ("
							+ jsonObject.getJSONObject("weight").get("imperial") + ")Pounds");

					information.append("\nHeight: (" + jsonObject.getJSONObject("height").get("metric") + ")CM OR ("
							+ jsonObject.getJSONObject("height").get("imperial") + ")Inches");

					if (jsonObject.has("bred_for") && !jsonObject.get("bred_for").equals("")) {
						information.append("\nBred for: " + jsonObject.get("bred_for").toString());
					}

					if (jsonObject.has("breed_group") && !jsonObject.get("breed_group").equals("")) {
						information.append("\nBreed Group: " + jsonObject.get("breed_group").toString());
					}

					information.append("\nLife Span: " + jsonObject.get("life_span").toString());

					if (jsonObject.has("temperament") && !jsonObject.get("temperament").equals("")) {
						information.append("\nTemperament: " + jsonObject.get("temperament").toString());
					}

					if (jsonObject.has("origin") && !jsonObject.get("origin").equals("")) {
						information.append("\nOrigin: " + jsonObject.get("origin").toString());
					}

					if (jsonObject.has("country_code") && !jsonObject.get("country_code").equals("")) {
						information.append("\nCountry: " + jsonObject.get("country_code").toString());
					}
				}
			}
			if(!jsonArray.toString().contains(dogName)) {
				JOptionPane.showMessageDialog(null, "No Records Found!");	//if the user input doesn't exists
			}
		} else {
			System.out.println("GET NOT WORKED");
		}
	}

}

<?php

/*********************************************************
 * Partie SERVEUR du service web RESTful. 
 *********************************************************/

require_once('./db_infos.php');

/**
 * Connexion au SGBD
 */

try {
	$pdo = new PDO(
		'mysql:host='.$host
		.';port='.$port
		.';dbname='.$dbname
		,$user
		,$pwd);
} catch (PDOException $e) {
	die("Erreur : " . $e->getMessage());
}

/**
 * Champs de la ressource ville
 */
$cols = array('Nom_Ville','MAJ','Code_Postal','Code_INSEE','Code_Region','Latitude','Longitude','Eloignement');

/**
 * Verbe HTTP
 */
$method = $_SERVER['REQUEST_METHOD'];

/**
 * Application du verbe sur la ressource
 */
switch($method) {

	case 'GET' : {		
		read_villes();		
	} break;
	case 'DELETE' : {
		delete_ville();
	} break;
	case 'PUT' : {
		update_ville();
	} break;
	case 'POST' : {
		create_ville();
	} break;
	default : {
		echo "méthode inconnue";
	}
	
}

/**
 * Exécute le verbe READ sur la ressource VILLES
 */
function read_villes() {
	global $pdo,$cols,$sql;

	if(isset($_GET['ville'])){
		$ville = $_GET['ville'];
	} else {
		$ville = '';
	}

	/**
		* Requète SQL
		*/

	$sql = "SELECT * FROM `villes` WHERE Nom_Ville LIKE \"%$ville%\" LIMIT 30";
	if($stmt = $pdo->query($sql)) {
		$items = $stmt->fetchAll(PDO::FETCH_ASSOC);
	}

	/**
		* Affichage terminal : présentation en JSON
		*/	
	
	header('Content-Type: application/json');		
	echo json_encode($items);

}

/**
 * Exécute le verbe DELETE sur la ressource VILLES
 */
function delete_ville() {
	global $pdo,$cols,$sql;

	if(isset($_GET['ville'])) {
		$ville = $_GET['ville'];

		/**
			* Requète SQL
			*/

		$sql = "DELETE FROM `villes` WHERE Nom_Ville LIKE \"%$ville%\"";
		$stmt = $pdo->query($sql);

		/**
			* Affichage terminal : présentation en JSON
			*/	
		
		header('Content-Type: application/json');		
		echo json_encode($pdo->errorInfo());

	} 
}

/**
 * Exécute le verbe POST sur la ressource VILLES
 */
function create_ville() {
	global $pdo,$cols,$sql;

	/**
		* Requète SQL
		*/

	$content = trim(file_get_contents("php://input"));
	$decoded = json_decode($content, true);

	$keys = '';
	$values = '';

	foreach ($decoded as $key => $value){
		$keys = $keys.$key.', ';
		$values = $values.'"'.$value.'", ';
	}

	$keys = substr($keys, 0, -2);
	$values = substr($values, 0, -2);

	$sql = "INSERT INTO `villes` ($keys) VALUES ($values)";
	$stmt = $pdo->query($sql);

	/**
		* Affichage terminal : présentation en JSON
		*/

	header('Content-Type: application/json');		
	echo json_encode($pdo->errorInfo());

}

/**
 * Exécute le verbe PUT sur la ressource VILLES
 */
function update_ville() {
	global $pdo,$cols,$sql;

		/**
			* Requète SQL
			*/

		$sql = "";
		$stmt = $pdo->query($sql);

		/**
			* Affichage terminal : présentation en JSON
			*/	

		header('Content-Type: application/json');		
		echo json_encode($pdo->errorInfo());

}

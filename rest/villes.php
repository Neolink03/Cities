<?php

/*********************************************************
 * Partie SERVEUR du service web RESTful. 
 *********************************************************/


/**
 * Connexion au SGBD
 */
try {
	$pdo = new PDO('mysql:host=localhost;port=3306;dbname=bd_rest', 'root', '');
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
//		delete_ville();
	} break;
	case 'PUT' : {
//		update_ville();
	} break;
	case 'POST' : {
//		create_ville();
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

	$sql = "SELECT * FROM `villes` WHERE Nom_Ville LIKE \"%$ville%\" LIMIT 10";
	
	if($stmt = $pdo->query($sql)) {
		$items = $stmt->fetchAll();
	}
	
	/**
		* Affichage terminal : présentation en JSON
		*/	
	header('Content-Type: application/json');		
	echo json_encode($items);
	// corrigé par Antunes Lonny

}


?>
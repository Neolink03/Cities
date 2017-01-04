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
$cols = array('Nom_Ville', 'MAJ', 'Code_Postal', 'Code_INSEE', 'Code_Region', 'Latitude', 'Longitude', 'Eloignement');

/**
 * Verbe HTTP
 */
$method = $_SERVER['REQUEST_METHOD'];

/**
 * Application du verbe sur la ressource
 */
switch ($method) {

    case 'GET' : {
        read_villes();
    }
        break;
    case 'DELETE' : {
        delete_ville();
    }
        break;
    case 'PUT' : {
        update_ville();
    }
        break;
    case 'POST' : {
        create_ville();
    }
        break;
    default : {
        echo "méthode inconnue";
    }

}

//region fonctions

/**
 * Exécute le verbe READ sur la ressource VILLES
 */

function read_villes()
{
    global $pdo, $cols, $sql;

    if (isset($_GET['ville'])) {
        $ville = $_GET['ville'];
    } else {
        $ville = '';
    }

    /**
     * Requète SQL
     */

    $sql = "SELECT * FROM `villes` WHERE Nom_Ville LIKE \"%$ville%\" LIMIT 10";
    if ($stmt = $pdo->query($sql)) {
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
function delete_ville()
{
    global $pdo, $cols, $sql;

    if (isset($_GET['ville'])) {
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
function create_ville()
{
    global $pdo, $cols, $sql;

    /**
     * Requète SQL
     */

    $content = trim(file_get_contents("php://input"));
    $decoded = json_decode($content, true);

    $keys = '';
    $values = '';

    foreach ($decoded as $key => $value) {
        $keys = $keys . $key . ', ';
        $values = $values . '"' . $value . '", ';
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
function update_ville()
{
    global $pdo, $cols, $sql;

    /**
     * Requète SQL
     */

    $content = trim(file_get_contents("php://input"));
    $decoded = json_decode($content, true);

    $update_set = '';

    foreach ($decoded as $key => $value) {
        $update_set = $update_set . $key . ' = ';
    }

    $update_set = substr($keys, 0, -2);

    print_r($update_set."\n");

    $sql = "UPDATE table
            SET colonne_1 = 'valeur 1', 
                colonne_2 = 'valeur 2', 
                colonne_3 = 'valeur 3'
            WHERE condition";
    $stmt = $pdo->query($sql);

    /**
     * Affichage terminal : présentation en JSON
     */

    header('Content-Type: application/json');
    echo json_encode($pdo->errorInfo());

}

//endregion
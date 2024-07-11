<?php
require_once __DIR__ . '/vendor/autoload.php';

use Victim\Application\Application;

$application = new Application();
$application->run();
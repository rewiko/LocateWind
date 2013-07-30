-- phpMyAdmin SQL Dump
-- version 3.3.7deb7
-- http://www.phpmyadmin.net
--
-- Serveur: localhost
-- Généré le : Dim 01 Avril 2012 à 11:50
-- Version du serveur: 5.1.61
-- Version de PHP: 5.3.3-7+squeeze8

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `Locate_Wind`
--

-- --------------------------------------------------------

--
-- Structure de la table `LOCATE`
--

CREATE TABLE IF NOT EXISTS `LOCATE` (
  `id_locate` int(11) NOT NULL AUTO_INCREMENT,
  `latitude` int(11) NOT NULL,
  `longitude` int(11) NOT NULL,
  `force` double NOT NULL,
  `orientation` varchar(50) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id_locate`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=127 ;





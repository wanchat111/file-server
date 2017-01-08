
--
-- Table structure for table `branch`
--

CREATE TABLE `branch` (
  `branch_id` int(11) NOT NULL,
  `branch_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `branch`
--

INSERT INTO `branch` (`branch_id`, `branch_name`) VALUES
(1, 'ส่วนกลาง'),
(2, 'ทวีรัตน์'),
(3, 'ท่าจีน'),
(4, 'บ้านคลองนกกระทุง'),
(5, 'เผยอิง'),
(6, 'ศิริพงศ์วิทยา'),
(7, 'สมานคุณ'),
(8, 'อนุบาลธิดาเมตตาธรรม');

-- --------------------------------------------------------

--
-- Table structure for table `file`
--

CREATE TABLE `file` (
  `file_id` int(11) NOT NULL,
  `file_name` varchar(50) NOT NULL,
  `folder_path` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `file`
--

INSERT INTO `file` (`file_id`, `file_name`, `folder_path`) VALUES
(1, 'หลักสูตรวิชาคอมพิวเตอร์พื้นฐาน', 'curriculum');

-- --------------------------------------------------------

--
-- Table structure for table `file_upload`
--

CREATE TABLE `file_upload` (
  `upload_id` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `description` text NOT NULL,
  `create_by` varchar(20) NOT NULL,
  `create_date` date NOT NULL,
  `last_modify` varchar(20) DEFAULT NULL,
  `date_modify` date DEFAULT NULL,
  `file_id` int(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `file_upload`
--

INSERT INTO `file_upload` (`upload_id`, `username`, `description`, `create_by`, `create_date`, `last_modify`, `date_modify`, `file_id`) VALUES
(1, 'wanchat', 'หลักสูตรวิชาคอมพิวเตอร์พื้นฐาน ปีการศึกษา 2559 ', 'Wanchat', '2016-11-27', NULL, NULL, 1);

-- --------------------------------------------------------

--
-- Table structure for table `folder`
--

CREATE TABLE `folder` (
  `folder_path` varchar(20) NOT NULL,
  `folder_name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `folder`
--

INSERT INTO `folder` (`folder_path`, `folder_name`) VALUES
('costs', 'ค่าใช้จ่าย'),
('curriculum', 'หลักสูตร'),
('information', 'รายละเอียดโรงเรียน'),
('schedule', 'ตารางสอน');

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`role_id`, `role_name`) VALUES
(1, 'admin'),
(2, 'useradmin'),
(3, 'user');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `username` varchar(20) NOT NULL,
  `role_id` int(11) NOT NULL,
  `branch_id` int(11) NOT NULL,
  `session_id` varchar(255) DEFAULT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `email` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`username`, `role_id`, `branch_id`, `session_id`, `password`, `name`, `surname`, `email`) VALUES
('wanchat', 1, 1, '', 'surachit', 'Wanchat', 'Damdoung', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `branch`
--
ALTER TABLE `branch`
  ADD PRIMARY KEY (`branch_id`);

--
-- Indexes for table `file`
--
ALTER TABLE `file`
  ADD PRIMARY KEY (`file_id`),
  ADD UNIQUE KEY `file_name` (`file_name`,`folder_path`),
  ADD KEY `index_folder_id` (`folder_path`);

--
-- Indexes for table `file_upload`
--
ALTER TABLE `file_upload`
  ADD PRIMARY KEY (`upload_id`),
  ADD UNIQUE KEY `unique_file_id` (`file_id`),
  ADD KEY `lnk_user_file_upload` (`username`);

--
-- Indexes for table `folder`
--
ALTER TABLE `folder`
  ADD PRIMARY KEY (`folder_path`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`role_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `branch`
--
ALTER TABLE `branch`
  MODIFY `branch_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- AUTO_INCREMENT for table `file`
--
ALTER TABLE `file`
  MODIFY `file_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `file_upload`
--
ALTER TABLE `file_upload`
  MODIFY `upload_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT for table `role`
--
ALTER TABLE `role`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `file_upload`
--
ALTER TABLE `file_upload`
  ADD CONSTRAINT `lnk_user_file_upload` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

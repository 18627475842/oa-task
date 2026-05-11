CREATE TABLE task_feedbacks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  task_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  tag ENUM('BLOCKING', 'PROGRESSING', 'NEED_RESOURCE', 'MILESTONE') DEFAULT 'PROGRESSING',
  mentioned_users VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (task_id) REFERENCES tasks(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_task_feedbacks_task_id ON task_feedbacks(task_id);
CREATE INDEX idx_task_feedbacks_created_at ON task_feedbacks(created_at DESC);

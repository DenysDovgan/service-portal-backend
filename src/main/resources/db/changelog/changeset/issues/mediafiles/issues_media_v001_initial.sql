CREATE TABLE issue_media (
                             id SERIAL PRIMARY KEY,
                             issue_id LONG NOT NULL,
                             file_url TEXT NOT NULL,  -- Stores cloud/local file path
                             file_type VARCHAR(10) CHECK (file_type IN ('IMAGE', 'VIDEO')),
                             uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (issue_id) REFERENCES issues(id) ON DELETE CASCADE
);
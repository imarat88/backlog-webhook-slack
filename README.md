# backlog-webhook-slack

Notify [slack](https://slack.com) of [Backlog](https://backlog.com/) changes.

## Requirements
- Docker
- Docker compose

## Usage

### Environments

|Name|Example|
|:---|:------|
|BACKLOG_URL|https://YOUR_SPACE_NAME.backlog.com|
|SLACK_URL||

### Example of docker command
    docker run -d --restart=always --name webhook -p 9000:9000 \
        -e BACKLOG_URL="https://YOUR_SPACE_NAME.backlog.com" \
        -e SLACK_URL="https://hooks.slack.com/services/XXXXXXXXX/YYYYYYYYY/ZZZZZZZZZ" \
        shomatan/backlog-webhook-slack
        
## Supported notifications
- [x] 1: Issue Created  
- [x] 2: Issue Updated  
- [x] 3: Issue Commented  
- [x] 4: Issue Deleted  
- [ ] 5: Wiki Created  
- [ ] 6: Wiki Updated  
- [ ] 7: Wiki Deleted  
- [ ] 8: File Added  
- [ ] 9: File Updated  
- [ ] 10: File Deleted  
- [ ] 11: SVN Committed  
- [ ] 12: Git Pushed  
- [ ] 13: Git Repository Created  
- [ ] 14: Issue Multi Updated  
- [ ] 15: Project User Added  
- [ ] 16: Project User Deleted  
- [ ] 17: Comment Notification Added  
- [x] 18: Pull Request Added  
- [x] 19: Pull Request Updated  
- [x] 20: Comment Added on Pull Request  
- [x] 21: Pull Request Deleted  
- [ ] 22: Milestone Created  
- [ ] 23: Milestone Updated  
- [ ] 24: Milestone Deleted  
- [ ] 25: Project Group Added  
- [ ] 26: Project Group Deleted   
     
## TODO
- Add test
- Support notifications
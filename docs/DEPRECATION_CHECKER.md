# Deprecation Checker Workflow

## Overview

The Deprecation Checker is a GitHub Actions workflow that automatically scans the codebase monthly to detect deprecation warnings and outdated dependencies. It helps maintain code quality by identifying deprecated APIs and library usage before they become breaking changes.

A status badge in the README shows the current state:
- **🟢 Green badge**: No deprecation warnings detected - codebase is clean
- **🔴 Red badge**: Deprecation warnings found - clicking the badge takes you to the open issue with details

## Features

- **Automated Monthly Checks**: Runs on the 1st of each month at 9:00 AM UTC
- **Manual Trigger**: Can be triggered manually via GitHub Actions UI
- **Multiple Detection Methods**:
  - Build-time deprecation warnings from Gradle
  - Android Lint deprecation checks
  - Outdated dependency detection
- **Automated Issue Management**: Creates or updates GitHub issues automatically
- **Detailed Reports**: Generates comprehensive reports saved as artifacts
- **Smart Issue Handling**: Updates existing issues instead of creating duplicates

## How It Works

### 1. Detection Process

The workflow performs three types of checks:

#### Build Deprecation Check
```bash
./gradlew build --warning-mode=all
```
- Runs a full project build with all warnings enabled
- Captures deprecation warnings from Kotlin and Java compilation
- Filters and extracts deprecation-related messages

#### Lint Deprecation Check
```bash
./gradlew lintDebug
```
- Runs Android Lint to detect deprecated API usage
- Scans for Android framework deprecations
- Checks for deprecated library methods

#### Dependency Updates Check
```bash
./gradlew dependencyUpdates
```
- Uses the dependency update plugin
- Identifies outdated dependencies
- Helps prevent using deprecated library versions

### 2. Report Generation

The workflow generates a comprehensive markdown report including:
- Summary of total deprecations found
- Build deprecation warnings
- Lint deprecation warnings
- Outdated dependencies list
- Action items checklist

### 3. GitHub Issue Creation

When deprecations are found:
- **First run**: Creates a new issue with labels `deprecation-warning` and `technical-debt`
- **Subsequent runs**: Updates the existing open issue with a new comment
- **No deprecations**: No issue is created/updated

### 4. Artifacts

All reports are uploaded as GitHub Actions artifacts:
- `build-output.log`: Complete build output
- `deprecations.txt`: Extracted build deprecations
- `lint-deprecations.txt`: Lint deprecation findings
- `outdated-deps.txt`: Outdated dependencies
- `deprecation-report.md`: Final formatted report

Artifacts are retained for 90 days.

## Configuration

### Schedule

The workflow runs on a cron schedule defined in the workflow file:

```yaml
schedule:
  - cron: '0 9 1 * *'  # 1st day of each month at 9:00 AM UTC
```

To change the schedule, edit the cron expression. Examples:
- `0 9 1,15 * *` - Run on 1st and 15th of each month
- `0 9 * * 1` - Run every Monday
- `0 9 1 */3 *` - Run quarterly (every 3 months)

Use [crontab.guru](https://crontab.guru/) to help create cron expressions.

### Manual Trigger

You can manually trigger the workflow:
1. Go to **Actions** tab in GitHub
2. Select **Monthly Deprecation Check** workflow
3. Click **Run workflow** button
4. Select the branch and click **Run workflow**

### Permissions

The workflow requires these permissions (already configured):
```yaml
permissions:
  issues: write      # To create/update issues
  contents: read     # To checkout repository
```

## Email Notifications

### Option 1: GitHub Watch Notifications

The easiest way to get notified is through GitHub's built-in notification system:

1. **Watch the Repository**:
   - Go to your repository
   - Click **Watch** → **Custom**
   - Enable **Issues** notifications

2. **Configure Email Preferences**:
   - Go to GitHub Settings → Notifications
   - Ensure "Email" is enabled
   - Set your notification preferences

You'll receive an email whenever the workflow creates or updates an issue.

### Option 2: Workflow Email Notifications

To add direct email notifications from the workflow, add this step before the issue creation:

```yaml
- name: Send Email Notification
  if: steps.summary.outputs.has_deprecations == 'true'
  uses: dawidd6/action-send-mail@v3
  with:
    server_address: smtp.gmail.com
    server_port: 465
    username: ${{ secrets.MAIL_USERNAME }}
    password: ${{ secrets.MAIL_PASSWORD }}
    subject: Deprecation Warnings Found in SimpleHIIT
    to: your-email@example.com
    from: GitHub Actions
    body: |
      ${{ env.REPORT_CONTENT }}

      View the full report: ${{ github.server_url }}/${{ github.repository }}/actions/runs/${{ github.run_id }}
```

**Required Secrets**:
- `MAIL_USERNAME`: Your SMTP username (e.g., gmail address)
- `MAIL_PASSWORD`: Your SMTP password or app password

**To add secrets**:
1. Go to repository **Settings** → **Secrets and variables** → **Actions**
2. Click **New repository secret**
3. Add `MAIL_USERNAME` and `MAIL_PASSWORD`

**Gmail Users**: You need to create an [App Password](https://support.google.com/accounts/answer/185833):
1. Enable 2-factor authentication
2. Go to Google Account → Security → App passwords
3. Generate an app password for "Mail"
4. Use this password in `MAIL_PASSWORD` secret

### Option 3: Slack/Discord Notifications

For team notifications, you can add Slack or Discord webhooks:

**Slack Example**:
```yaml
- name: Notify Slack
  if: steps.summary.outputs.has_deprecations == 'true'
  uses: slackapi/slack-github-action@v1.24.0
  with:
    webhook: ${{ secrets.SLACK_WEBHOOK }}
    webhook-type: incoming-webhook
    payload: |
      {
        "text": "⚠️ Deprecation warnings detected in SimpleHIIT",
        "blocks": [
          {
            "type": "section",
            "text": {
              "type": "mrkdwn",
              "text": "*${{ steps.summary.outputs.total_count }}* deprecation warning(s) found"
            }
          }
        ]
      }
```

**Required Secret**: `SLACK_WEBHOOK` - Your Slack incoming webhook URL

## Troubleshooting

### Workflow Not Running

- Check that the workflow file is on the default branch (usually `main` or `master`)
- Verify the repository has Actions enabled (Settings → Actions)
- Check the Actions tab for any errors

### No Deprecations Detected

- The workflow might need customization for your specific build
- Check the uploaded artifacts to see the raw build output
- Verify the grep patterns match your deprecation format

### Build Failures

- The workflow uses `continue-on-error: true` to proceed even if build fails
- Check the workflow run logs for specific errors
- Ensure the project builds successfully locally first

### Issue Not Created

- Verify the workflow has `issues: write` permission
- Check if there's already an open issue with the `deprecation-warning` label
- Review the workflow run logs for errors in the "Create GitHub Issue" step

## Labels

The workflow uses these labels for issues:
- `deprecation-warning`: Primary label for deprecation issues
- `technical-debt`: Secondary label indicating maintenance work

**To create these labels** (if they don't exist):
1. Go to repository → **Issues** → **Labels**
2. Click **New label**
3. Create:
   - Name: `deprecation-warning`, Color: `#d73a4a` (red)
   - Name: `technical-debt`, Color: `#fbca04` (yellow)

## Best Practices

1. **Review Regularly**: Don't wait for the monthly check - review deprecations as they appear
2. **Prioritize**: Focus on deprecations that will be removed in upcoming releases
3. **Test Thoroughly**: Always test after updating deprecated code
4. **Close Issues**: Close the deprecation issue once all warnings are resolved
5. **Update Dependencies**: Keep dependencies current to avoid deprecated versions

## Customization

### Adjusting Report Content

Edit the "Generate Report Summary" step to customize the report format or add additional sections.

### Adding More Checks

Add additional steps to check for:
- Specific deprecated libraries
- Custom deprecation patterns
- Security vulnerabilities
- Code quality metrics

### Filtering Warnings

If you need to ignore certain deprecations, add filtering in the build step:
```bash
grep -v "specific-pattern-to-ignore" build-reports/deprecations.txt
```

## Related Documentation

- [CI Workflows](CI_WORKFLOWS.md)
- [Gradle Tasks](GRADLE_TASKS.md)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)
